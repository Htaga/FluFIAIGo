package com.example.flufiaigo

data class NominaModel(
    val id: String,
    val empleadoNombre: String,
    val sueldoBase: Double,
    val horasExtra: Double,
    val bonificaciones: Double,
    val impuestos: Double,
    val seguridadSocial: Double,
    val otrosDeducciones: Double,
    val fecha: Long = System.currentTimeMillis(),
    val divisa: String = "€"
) {
    // Cuando se acceden a estas variables se calcula su valor
    val salarioBruto: Double 
        get() = sueldoBase + horasExtra + bonificaciones

    val salarioNeto: Double 
        get() = salarioBruto - (impuestos + seguridadSocial + otrosDeducciones)
}
