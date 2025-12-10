package com.example.medicamentos.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.medicamentos.data.entities.MedicamentoEntity

@Dao
interface MedicamentoDao {

    @Query("SELECT * FROM medicamentos ORDER BY nome")
    fun getAllFlow(): Flow<List<MedicamentoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(medicamento: MedicamentoEntity): Long

    @Update
    suspend fun update(medicamento: MedicamentoEntity)

    @Delete
    suspend fun delete(medicamento: MedicamentoEntity)
}

