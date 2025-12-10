package com.example.medicamentos.data.entities
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medicamentos")
data class MedicamentoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nome: String,
    val dataInicio: String,
    val hora: String,
    val frequencia: String,
    val dataFinal: String = "",
    val descricao: String = ""
)

