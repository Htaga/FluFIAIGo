package com.example.flufiaigo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.Date
import java.util.UUID

class IngresoViewModel : ViewModel() {

    // 1. DATOS DE ENTRADA (Input del usuario)
    val concepto = MutableLiveData<String>("")
    val importe = MutableLiveData<String>("")

    val metodoCobro = MutableLiveData<String>("")
    
    // Variables para la fecha
    val fechaReal = MutableLiveData<Date>(Date())
    val fechaTexto = MutableLiveData<String>("")

    // 2. RESULTADO PROTEGIDO
    private val _ingresoActual = MutableLiveData<IngresoModel?>()
    val ingresoActual: LiveData<IngresoModel?> get() = _ingresoActual

    fun crearIngreso() {
        val importe = importe.value?.toDoubleOrNull() ?: 0.0
        val concepto = concepto.value ?: "-"
        val fecha = fechaReal.value ?: Date()

        val nuevoIngreso = IngresoModel(
            id = UUID.randomUUID().toString(),
            fecha = fecha,
            concepto = concepto,
            importe = importe,
            metodoPago = metodoCobro.value ?: "-"
        )

        _ingresoActual.value = nuevoIngreso
    }
}
