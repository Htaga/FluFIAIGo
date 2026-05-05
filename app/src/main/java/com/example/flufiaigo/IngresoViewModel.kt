package com.example.flufiaigo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

class IngresoViewModel(private val dao: IngresoDao) : ViewModel() {

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

        // Guardar el ingreso en la base de datos usando corrutinas
        viewModelScope.launch {
            dao.insertIngreso(nuevoIngreso)
            // Actualizamos la variable para notificar a la vista que ya se guardó
            _ingresoActual.value = nuevoIngreso
        }
    }
}

class IngresoViewModelFactory(private val dao: IngresoDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(IngresoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return IngresoViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
