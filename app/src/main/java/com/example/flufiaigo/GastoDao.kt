package com.example.flufiaigo

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface GastoDao {
    @Query("SELECT * FROM gastos ORDER BY fecha DESC")
    fun getAllGastos(): LiveData<List<GastoModel>>

    @Query("SELECT * FROM gastos WHERE fecha BETWEEN :startDate AND :endDate ORDER BY fecha DESC")
    fun getGastosBetweenDates(startDate: Long, endDate: Long): LiveData<List<GastoModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGasto(gasto: GastoModel)

    @Delete
    suspend fun deleteGasto(gasto: GastoModel)

}
