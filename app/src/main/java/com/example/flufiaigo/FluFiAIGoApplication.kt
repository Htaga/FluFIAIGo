package com.example.flufiaigo

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class FluFIAIGoApplication : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy { FluFiAIGoDatabase.getInstance(this) }
    
    val ingresoRepository by lazy { IngresoRepository(database.ingresoDao) }
    val gastoRepository by lazy { GastoRepository(database.gastoDao) }
    val nominaRepository by lazy { NominaRepository(database.nominaDao) }

    override fun onCreate(){
        super.onCreate()
    }
}