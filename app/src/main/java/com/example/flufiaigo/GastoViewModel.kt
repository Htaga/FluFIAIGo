package com.example.flufiaigo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

class GastoViewModel(private val dao: GastoDao) : ViewModel() {

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

        // Guardar el gasto en la base de datos usando corrutinas
        viewModelScope.launch {
            dao.insertGasto(nuevoGasto)
            // Actualizamos la variable para notificar a la vista que ya se guardó
            _gastoActual.value = nuevoGasto
        }
    }
}

class GastoViewModelFactory(private val dao: GastoDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GastoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GastoViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
