package com.example.flufiaigo

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity(tableName = "nominas")
data class NominaModel(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val empleadoNombre: String,
    val sueldoBase: Double,
    val horasExtra: Double,
    val bonificaciones: Double,
    val impuestos: Double,
    val seguridadSocial: Double,
    val otrosDeducciones: Double,
    val fecha: Date = Date(),
    val divisa: String = "€"
) {
    val salarioBruto: Double 
        get() = sueldoBase + horasExtra + bonificaciones

    val salarioNeto: Double 
        get() = salarioBruto - (impuestos + seguridadSocial + otrosDeducciones)
}
