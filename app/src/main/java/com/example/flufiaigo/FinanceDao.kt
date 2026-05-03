package com.example.flufiaigo

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FinanceDao {
    // Ingresos
    @Query("SELECT * FROM ingresos ORDER BY fecha DESC")
    fun getAllIngresos(): LiveData<List<IngresoModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngreso(ingreso: IngresoModel)

    @Delete
    suspend fun deleteIngreso(ingreso: IngresoModel)

    // Gastos
    @Query("SELECT * FROM gastos ORDER BY fecha DESC")
    fun getAllGastos(): LiveData<List<GastoModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGasto(gasto: GastoModel)

    @Delete
    suspend fun deleteGasto(gasto: GastoModel)

    // Nominas
    @Query("SELECT * FROM nominas ORDER BY fecha DESC")
    fun getAllNominas(): LiveData<List<NominaModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNomina(nomina: NominaModel)

    @Delete
    suspend fun deleteNomina(nomina: NominaModel)

    //Todos los movimientos
    @Query("SELECT * FROM ingresos UNION SELECT * FROM gastos ORDER BY fecha DESC")
    fun getAllMovimientos(): LiveData<List<Entrada>>
}
