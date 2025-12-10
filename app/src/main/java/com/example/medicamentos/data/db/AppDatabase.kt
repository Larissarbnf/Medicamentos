package com.example.medicamentos.data.db

import androidx.room.*
import com.example.medicamentos.data.dao.MedicamentoDao
import com.example.medicamentos.data.entities.MedicamentoEntity

@Database(entities = [MedicamentoEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun medicamentoDao(): MedicamentoDao
}