package com.example.flufiaigo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.Date
import java.util.UUID

class GastoViewModel : ViewModel() {

    // 1. DATOS DE ENTRADA (Input del usuario)
    val concepto = MutableLiveData<String>("")
    val importe = MutableLiveData<String>("")

    val metodoPago = MutableLiveData<String>("")
    
    // Variables para la fecha
    val fechaReal = MutableLiveData<Date>(Date())
    val fechaTexto = MutableLiveData<String>("")

    // 2. RESULTADO PROTEGIDO
    private val _gastoActual = MutableLiveData<GastoModel?>()
    val gastoActual: LiveData<GastoModel?> get() = _gastoActual

    fun crearGasto() {
        val importe = importe.value?.toDoubleOrNull() ?: 0.0
        val concepto = concepto.value ?: "-"
        val fecha = fechaReal.value ?: Date()

        val nuevoGasto = GastoModel(
            id = UUID.randomUUID().toString(),
            fecha = fecha,
            concepto = concepto,
            importe = importe,
            metodoPago = metodoPago.value ?: "-"
        )

        _gastoActual.value = nuevoGasto
    }
}
