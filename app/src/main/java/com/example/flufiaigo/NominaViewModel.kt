package com.example.flufiaigo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.Date
import java.util.UUID

class NominaViewModel : ViewModel() {

    // 1. DATOS DE ENTRADA (Input del usuario)
    val nombreEmpleado = MutableLiveData<String>("")
    val salarioBruto = MutableLiveData<String>("")
    val retenciones = MutableLiveData<String>("")

    // 2. RESULTADO PROTEGIDO
    // Propiedad privada: el ViewModel puede modificarla internamente.
    private val _nominaActual = MutableLiveData<NominaModel?>()

    // Propiedad pública: el Fragmento solo puede "observar" (solo lectura).
    val nominaActual: LiveData<NominaModel?> get() = _nominaActual

    fun calcularYGenerarNomina() {
        val bruto = salarioBruto.value?.toDoubleOrNull() ?: 0.0
        val deduccion = retenciones.value?.toDoubleOrNull() ?: 0.0
        val neto = bruto - deduccion

        // Se crea el objeto del modelo con los datos actuales
        val nuevaNomina = NominaModel(
            id = UUID.randomUUID().toString(),
            empleadoNombre = nombreEmpleado.value ?: "Sin nombre",
            sueldoBase = bruto,
            deducciones = deduccion,
            bonificaciones = 0.0,
            fecha = Date(),
            netoARecibir = neto
        )

        // SÓLO el ViewModel tiene acceso a .value para escribir a través de la propiedad privada
        _nominaActual.value = nuevaNomina
    }
}