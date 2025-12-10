# 💊 Minhas Pílulas

Um aplicativo Android moderno para gerenciamento de medicamentos, desenvolvido com Jetpack Compose e Room Database.

## 📱 Sobre o Projeto

O **Minhas Pílulas** permite que usuários organizem seus medicamentos de forma simples e intuitiva, registrando informações como nome, horário de uso, frequência e descrição. Todos os dados são armazenados localmente no dispositivo, garantindo privacidade e acesso offline.

## ✨ Funcionalidades

- ✅ Adicionar medicamentos com informações detalhadas
- ✅ Editar medicamentos existentes
- ✅ Excluir medicamentos
- ✅ Visualizar lista completa de medicamentos
- ✅ Atualização automática da interface em tempo real
- ✅ Tema claro e escuro
- ✅ Persistência local de dados

## 🛠️ Tecnologias Utilizadas

- **Kotlin** - Linguagem de programação
- **Jetpack Compose** - UI moderna e declarativa
- **Room Database** - Persistência local (SQLite)
- **Coroutines** - Programação assíncrona
- **Flow** - Dados reativos
- **DataStore** - Preferências do usuário
- **Material Design 3** - Design system

## 🏗️ Arquitetura

O projeto segue boas práticas de arquitetura Android:

```
app/
├── data/
│   ├── entities/
│   │   └── MedicamentoEntity.kt      # Definição da tabela
│   ├── dao/
│   │   └── MedicamentoDao.kt         # Operações CRUD
│   ├── db/
│   │   ├── AppDatabase.kt            # Configuração do banco
│   │   └── DatabaseProvider.kt       # Singleton do banco
│   └── datastore/
│       └── SettingsDataStore.kt      # Preferências
└── MainActivity.kt                    # UI principal
```

### Padrões Utilizados

- **Repository Pattern** - Separação de responsabilidades
- **Singleton Pattern** - Instância única do banco de dados
- **MVVM (implícito)** - Separação entre UI e lógica de negócios

## 🚀 Como Executar

1. Clone o repositório:
```bash
git clone https://github.com/seu-usuario/minhas-pilulas.git
```

2. Abra o projeto no Android Studio

3. Sincronize as dependências do Gradle

4. Execute o aplicativo em um emulador ou dispositivo físico

### Requisitos

- Android Studio Arctic Fox ou superior
- SDK mínimo: Android 7.0 (API 24)
- SDK alvo: Android 14 (API 34)

## 💡 Como Funciona

### Salvamento de Dados

1. Usuário preenche o formulário com informações do medicamento
2. Ao clicar no botão de salvar, os dados são validados
3. Uma coroutine executa a operação de inserção em background
4. Room Database salva os dados no arquivo local SQLite
5. Flow notifica a UI automaticamente sobre as mudanças
6. A lista é atualizada em tempo real sem necessidade de refresh manual

### Estrutura do Banco de Dados

**Tabela: medicamentos**

| Campo       | Tipo   | Descrição                          |
|-------------|--------|------------------------------------|
| id          | Long   | Chave primária (auto incremento)   |
| nome        | String | Nome do medicamento                |
| dataInicio  | String | Data de início do tratamento       |
| hora        | String | Horário de uso                     |
| frequencia  | String | Frequência (diária, limitada, etc) |
| dataFinal   | String | Data final (opcional)              |
| descricao   | String | Observações adicionais             |

## 🎨 Interface

O aplicativo possui uma interface clean e intuitiva com:

- **Tela de Lista**: Visualização de todos os medicamentos cadastrados
- **Tela de Adição/Edição**: Formulário completo para gerenciar medicamentos
- **FAB (Floating Action Button)**: Acesso rápido para adicionar medicamentos
- **Tema Adaptativo**: Suporte a modo claro e escuro

## 🔄 Fluxo de Dados Reativo

```
Usuário → Formulário → Validação → Coroutine → Room Database
                                                      ↓
                                                    Flow
                                                      ↓
                                     UI (Atualização Automática)
```

## 📚 Conceitos Implementados

### Coroutines
Operações de banco de dados são executadas em background para manter a UI responsiva e evitar travamentos.

### Flow
Implementação de dados reativos que atualizam a interface automaticamente quando há mudanças no banco de dados.

### Room Database
ORM (Object-Relational Mapping) que simplifica operações com SQLite, oferecendo:
- Verificação de erros em tempo de compilação
- Código mais limpo e seguro
- Suporte nativo a Flow e Coroutines

## 🐛 Solução de Problemas

### App crasha ao abrir
- Verifique se todas as dependências estão atualizadas
- Limpe e reconstrua o projeto (`Build > Clean Project`)

### Medicamentos não aparecem após salvar
- Verifique se o Flow está sendo coletado corretamente
- Confirme que as coroutines estão sendo executadas no escopo adequado


