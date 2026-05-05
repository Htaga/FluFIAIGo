package com.example.flufiaigo
import android.app.Application

class FluFIAIGoApplication : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy { AppDatabase.getInstance(this) }
    override fun onCreate(){
        super.onCreate()
    }
}