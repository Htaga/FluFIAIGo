package com.example.flufiaigo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.Date
import java.util.UUID

class NominaViewModel : ViewModel() {
    
    // 1. Datos de entrada (lo que el usuario escribe)
    val nombreEmpleado = MutableLiveData<String>("") 
    val salarioBruto = MutableLiveData<String>("")
    val retenciones = MutableLiveData<String>("")
    val nominaActual = MutableLiveData<NominaModel?>()

    //poc, no es la version final
    fun calcularYGenerarNomina() {
        val bruto = salarioBruto.value?.toDoubleOrNull() ?: 0.0
        val deduccion = retenciones.value?.toDoubleOrNull() ?: 0.0
        val neto = bruto - deduccion

        // Se crea el modelo de la nomina con los datos que introduce el usuario
        val nuevaNomina = NominaModel(
            id = UUID.randomUUID().toString(),
            empleadoNombre = nombreEmpleado.value ?: "Sin nombre",
            sueldoBase = bruto,
            deducciones = deduccion,
            bonificaciones = 0.0,
            fecha = Date(),
            netoARecibir = neto
        )
        nominaActual.value = nuevaNomina
    }
}