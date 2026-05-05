package com.example.flufiaigo

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface IngresoDao {
    // Ingresos
    @Query("SELECT * FROM ingresos ORDER BY fecha DESC")
    fun getAllIngresos(): LiveData<List<IngresoModel>>

    @Query("SELECT * FROM ingresos WHERE fecha BETWEEN :startDate AND :endDate ORDER BY fecha DESC")
    fun getIngresosBetweenDates(startDate: Long, endDate: Long): LiveData<List<IngresoModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngreso(ingreso: IngresoModel)

    @Delete
    suspend fun deleteIngreso(ingreso: IngresoModel)
}
