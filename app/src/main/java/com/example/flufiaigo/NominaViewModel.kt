package com.example.flufiaigo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NominaViewModel : ViewModel() {
    // LiveData que guardarán lo que el usuario escriba
    val salarioBruto = MutableLiveData<String>("")
    val retenciones = MutableLiveData<String>("")

    // LiveData para el resultado calculado
    val salarioNetoCalculado = MutableLiveData<String>("0.00")

    // Función que llamaremos cuando cambien los valores
    fun calcularNeto() {
        val bruto = salarioBruto.value?.toDoubleOrNull() ?: 0.0
        val retencion = retenciones.value?.toDoubleOrNull() ?: 0.0

        val neto = bruto - retencion
        salarioNetoCalculado.value = String.format("%.2f", neto)
    }
}