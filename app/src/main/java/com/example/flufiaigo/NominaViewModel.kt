package com.example.flufiaigo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.Date
import java.util.UUID

class NominaViewModel : ViewModel() {

    // 1. DATOS DE ENTRADA (Input del usuario)
    val nombreEmpleado = MutableLiveData<String>("")
    val sueldoBase = MutableLiveData<String>("")
    val horasExtra = MutableLiveData<String>("")
    val bonificaciones = MutableLiveData<String>("")
    val impuestos = MutableLiveData<String>("")
    val seguridadSocial = MutableLiveData<String>("")
    val otrosDeducciones = MutableLiveData<String>("")

    // 2. RESULTADO PROTEGIDO
    private val _nominaActual = MutableLiveData<NominaModel?>()
    val nominaActual: LiveData<NominaModel?> get() = _nominaActual

    fun calcularYGenerarNomina() {
        val base = sueldoBase.value?.toDoubleOrNull() ?: 0.0
        val extra = horasExtra.value?.toDoubleOrNull() ?: 0.0
        val bonos = bonificaciones.value?.toDoubleOrNull() ?: 0.0
        val imp = impuestos.value?.toDoubleOrNull() ?: 0.0
        val ss = seguridadSocial.value?.toDoubleOrNull() ?: 0.0
        val otros = otrosDeducciones.value?.toDoubleOrNull() ?: 0.0

        // Se crea el objeto del modelo con los datos actuales
        val nuevaNomina = NominaModel(
            id = UUID.randomUUID().toString(),
            empleadoNombre = nombreEmpleado.value ?: "Sin nombre",
            sueldoBase = base,
            horasExtra = extra,
            bonificaciones = bonos,
            impuestos = imp,
            seguridadSocial = ss,
            otrosDeducciones = otros,
            fecha = Date()
        )

        // SÓLO el ViewModel tiene acceso a .value para escribir a través de la propiedad privada
        _nominaActual.value = nuevaNomina
    }
}