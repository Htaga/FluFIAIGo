package com.example.flufiaigo

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NominaDao {
    @Query("SELECT * FROM nominas ORDER BY fecha DESC")
    fun getAllNominas(): LiveData<List<NominaModel>>

    @Query("SELECT * FROM nominas WHERE fecha BETWEEN :startDate AND :endDate ORDER BY fecha DESC")
    fun getNominasBetweenDates(startDate: Long, endDate: Long): LiveData<List<NominaModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNomina(nomina: NominaModel)

    @Delete
    suspend fun deleteNomina(nomina: NominaModel)
}
