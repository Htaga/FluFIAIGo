package com.example.flufiaigo

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentSnapshot
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

fun NominaModel.toMap() : Map<String, Any?> = mapOf(
    "id" to id,
    "empleadoNombre" to empleadoNombre,
    "sueldoBase" to sueldoBase,
    "horasExtra" to horasExtra,
    "bonificaciones" to bonificaciones,
    "impuestos" to impuestos,
    "seguridadSocial" to seguridadSocial,
    "otrosDeducciones" to otrosDeducciones,
    "fecha" to fecha,
    "divisa" to divisa
)

fun DocumentSnapshot.toNominaModel() : NominaModel? {
    return try {
        NominaModel(
            id = getString("id") ?: return null,
            empleadoNombre = getString("empleadoNombre") ?: "",
            sueldoBase = getDouble("sueldoBase") ?: 0.0,
            horasExtra = getDouble("horasExtra") ?: 0.0,
            bonificaciones = getDouble("bonificaciones") ?: 0.0,
            impuestos = getDouble("impuestos") ?: 0.0,
            seguridadSocial = getDouble("seguridadSocial") ?: 0.0,
            otrosDeducciones = getDouble("otrosDeducciones") ?: 0.0,
            fecha = getDate("fecha") ?: Date(),
            divisa = getString("divisa") ?: "€"
        )
    } catch (e: Exception) { null }
}
