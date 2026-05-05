package com.example.flufiaigo

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class FluFIAIGoApplication : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy { FluFiAIGoDatabase.getInstance(this) }
    override fun onCreate(){
        super.onCreate()
    }
}