package com.example.flufiaigo

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentSnapshot
import java.util.Date
import java.util.UUID

@Entity(tableName = "ingresos")
data class IngresoModel(
    @PrimaryKey
    override val id: String = UUID.randomUUID().toString(),
    override val fecha: Date = Date(),
    override val concepto: String,
    override val importe: Double,
    override val metodoPago: String,
    override val tipo: String = "ingreso"
) : Entrada(id, fecha, concepto, importe, metodoPago, tipo)

fun IngresoModel.toMap() : Map<String, Any?> = mapOf(
    "id" to id,
    "fecha" to fecha,
    "concepto" to concepto,
    "importe" to importe,
    "metodoPago" to metodoPago,
    "tipo" to tipo
)

fun DocumentSnapshot.toIngresoModel() : IngresoModel? {
    return try {
        IngresoModel(
            id = getString("id") ?: return null,
            fecha = getDate("fecha") ?: Date(),
            concepto = getString("concepto") ?: "",
            importe = getDouble("importe") ?: 0.0,
            metodoPago = getString("metodoPago") ?: "",
            tipo = getString("tipo") ?: ""
        )
    } catch (e: Exception) { null}
    }