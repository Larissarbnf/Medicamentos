package com.example.medicamentos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import com.example.medicamentos.data.db.DatabaseProvider
import com.example.medicamentos.data.entities.MedicamentoEntity
import com.example.medicamentos.datastore.darkModeFlow
import com.example.medicamentos.datastore.saveDarkMode

// Re-usable model alias: use the Entity directly in UI
typealias Medicamento = MedicamentoEntity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Obtain dark mode pref via DataStore
            val context = LocalContext.current
            val darkModeFlow = darkModeFlow(context)
            val darkMode by darkModeFlow.collectAsState(initial = true)

            MaterialTheme(
                colorScheme = if (darkMode) {
                    darkColorScheme(
                        primary = Color(0xFF6B1B9A),
                        secondary = Color(0xFFE91E63),
                        background = Color(0xFF121212),
                        surface = Color(0xFF1E1E1E)
                    )
                } else {
                    lightColorScheme()
                }
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LembreteRemedioApp()
                }
            }
        }
    }
}

@Composable
fun LembreteRemedioApp() {
    val context = LocalContext.current

    // Carrega o DAO de forma assíncrona para não travar a UI
    val dao = remember {
        DatabaseProvider.getDatabase(context.applicationContext).medicamentoDao()
    }

    // Coleta os medicamentos de forma assíncrona
    val medicamentos by dao.getAllFlow().collectAsState(initial = emptyList())

    val scope = rememberCoroutineScope()

    var telaAtual by remember { mutableStateOf("lista") }
    var medicamentoEditando by remember { mutableStateOf<Medicamento?>(null) }

    // For theme toggle in UI
    val darkMode by darkModeFlow(context).collectAsState(initial = true)

    when (telaAtual) {
        "lista" -> TelaListaMedicamentos(
            medicamentos = medicamentos,
            darkMode = darkMode,
            onToggleTheme = { enabled ->
                scope.launch { saveDarkMode(context, enabled) }
            },
            onAddClick = {
                medicamentoEditando = null
                telaAtual = "adicionar"
            },
            onEditClick = { medicamento ->
                medicamentoEditando = medicamento
                telaAtual = "adicionar"
            },
            onDeleteClick = { medicamento ->
                scope.launch {
                    dao.delete(medicamento)
                }
            }
        )
        "adicionar" -> TelaAdicionarMedicamento(
            medicamentoEditando = medicamentoEditando,
            onSave = { medicamento ->
                scope.launch {
                    if (medicamentoEditando == null) {
                        // Novo medicamento - deixa o Room gerar o ID automaticamente
                        dao.insert(medicamento)
                    } else {
                        // Editando medicamento existente
                        dao.update(medicamento)
                    }
                }
                telaAtual = "lista"
            },
            onBack = { telaAtual = "lista" }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaListaMedicamentos(
    medicamentos: List<Medicamento>,
    darkMode: Boolean,
    onToggleTheme: (Boolean) -> Unit,
    onAddClick: () -> Unit,
    onEditClick: (Medicamento) -> Unit,
    onDeleteClick: (Medicamento) -> Unit
) {
    // Obtém a data atual
    val dataAtual = remember {
        val sdf = SimpleDateFormat("dd 'de' MMMM", Locale("pt", "BR"))
        sdf.format(Date())
    }

    val diaSemana = remember {
        val sdf = SimpleDateFormat("EEEE", Locale("pt", "BR"))
        sdf.format(Date())
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "MINHAS PÍLULAS",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF6B1B9A),
                    titleContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            Icons.Default.Menu,
                            contentDescription = "Menu",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    // Theme toggle
                    IconButton(onClick = { onToggleTheme(!darkMode) }) {
                        Text(
                            text = if (darkMode) "🌙" else "☀️",
                            fontSize = 24.sp,
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                    IconButton(onClick = {}) {
                        Icon(
                            Icons.Default.Refresh,
                            contentDescription = "Atualizar",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = Color(0xFFE91E63)
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Adicionar",
                    tint = Color.White
                )
            }
        },
        containerColor = Color(0xFF121212)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Data atual
            Surface(
                color = Color(0xFF6B1B9A),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        dataAtual,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        diaSemana,
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 16.sp
                    )
                }
            }

            // Lista de medicamentos
            if (medicamentos.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.Notifications,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Sem pílulas programadas",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    item {
                        Text(
                            "Lista de Medicamentos",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                    }

                    items(medicamentos) { medicamento ->
                        CardMedicamento(
                            medicamento = medicamento,
                            onEdit = { onEditClick(medicamento) },
                            onDelete = { onDeleteClick(medicamento) }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun CardMedicamento(
    medicamento: Medicamento,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFF1E1E1E),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    Icons.Default.Favorite,
                    contentDescription = null,
                    tint = Color(0xFFE91E63),
                    modifier = Modifier.size(32.dp)
                )

                Column {
                    Text(
                        medicamento.nome,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        "${medicamento.hora} • ${
                            if (medicamento.frequencia == "diariamente")
                                "Diariamente"
                            else
                                "Com pausas"
                        }",
                        color = Color.Gray,
                        fontSize = 13.sp
                    )
                    if (medicamento.descricao.isNotEmpty()) {
                        Text(
                            medicamento.descricao,
                            color = Color.Gray.copy(alpha = 0.7f),
                            fontSize = 11.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }

            Row {
                IconButton(onClick = onEdit) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Editar",
                        tint = Color(0xFF2196F3)
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Excluir",
                        tint = Color(0xFFF44336)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaAdicionarMedicamento(
    medicamentoEditando: Medicamento?,
    onSave: (Medicamento) -> Unit,
    onBack: () -> Unit
) {
    var nome by remember { mutableStateOf(medicamentoEditando?.nome ?: "") }
    var dataInicio by remember { mutableStateOf(medicamentoEditando?.dataInicio ?: "") }
    var hora by remember { mutableStateOf(medicamentoEditando?.hora ?: "") }
    var frequencia by remember { mutableStateOf(medicamentoEditando?.frequencia ?: "diariamente") }
    var dataFinal by remember { mutableStateOf(medicamentoEditando?.dataFinal ?: "") }
    var descricao by remember { mutableStateOf(medicamentoEditando?.descricao ?: "") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "MINHAS PÍLULAS",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF6B1B9A),
                    titleContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (nome.isNotEmpty() && dataInicio.isNotEmpty() && hora.isNotEmpty()) {
                        val medicamentoParaSalvar = if (medicamentoEditando != null) {
                            // Editando: mantém o ID original
                            Medicamento(
                                id = medicamentoEditando.id,
                                nome = nome,
                                dataInicio = dataInicio,
                                hora = hora,
                                frequencia = frequencia,
                                dataFinal = dataFinal,
                                descricao = descricao
                            )
                        } else {
                            // Novo: ID = 0 para auto-incrementar
                            Medicamento(
                                id = 0L,
                                nome = nome,
                                dataInicio = dataInicio,
                                hora = hora,
                                frequencia = frequencia,
                                dataFinal = dataFinal,
                                descricao = descricao
                            )
                        }
                        onSave(medicamentoParaSalvar)
                    }
                },
                containerColor = Color(0xFFE91E63)
            ) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = "Salvar",
                    tint = Color.White
                )
            }
        },
        containerColor = Color(0xFF121212)
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(vertical = 24.dp)
        ) {
            item {
                CampoTexto(
                    label = "Remédio *",
                    value = nome,
                    onValueChange = { nome = it },
                    placeholder = "Nome do medicamento"
                )
            }

            item {
                CampoTexto(
                    label = "Data de início *",
                    value = dataInicio,
                    onValueChange = { dataInicio = it },
                    placeholder = "DD/MM/AAAA"
                )
            }

            item {
                CampoTexto(
                    label = "Hora *",
                    value = hora,
                    onValueChange = { hora = it },
                    placeholder = "00:00",
                    destaque = true
                )
            }

            item {
                Column {
                    Text(
                        "Dias *",
                        color = Color.White,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    OpcaoRadio(
                        texto = "Diariamente e permanentemente",
                        selecionado = frequencia == "diariamente",
                        onClick = { frequencia = "diariamente" }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OpcaoRadio(
                        texto = "Limitado ou com pausas",
                        selecionado = frequencia == "limitado",
                        onClick = { frequencia = "limitado" }
                    )
                }
            }

            item {
                CampoTexto(
                    label = "Data final",
                    value = dataFinal,
                    onValueChange = { dataFinal = it },
                    placeholder = "DD/MM/AAAA"
                )
            }

            item {
                Column {
                    Text(
                        "Descrição",
                        color = Color.White,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    OutlinedTextField(
                        value = descricao,
                        onValueChange = { descricao = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF1E1E1E),
                            unfocusedContainerColor = Color(0xFF1E1E1E),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color(0xFFE91E63),
                            unfocusedBorderColor = Color.Gray
                        ),
                        placeholder = {
                            Text(
                                "Informações adicionais (opcional)",
                                color = Color.Gray
                            )
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampoTexto(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    destaque: Boolean = false
) {
    Column {
        Text(
            label,
            color = Color.White,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFF1E1E1E),
                unfocusedContainerColor = Color(0xFF1E1E1E),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = if (destaque) Color(0xFFE91E63) else Color(0xFFE91E63),
                unfocusedBorderColor = if (destaque) Color(0xFFE91E63) else Color.Gray
            ),
            placeholder = {
                Text(placeholder, color = Color.Gray)
            }
        )
    }
}

@Composable
fun OpcaoRadio(
    texto: String,
    selecionado: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selecionado,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = Color(0xFFE91E63),
                unselectedColor = Color.Gray
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(texto, color = Color.White)
    }
}