package com.example.flufiaigo

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentSnapshot
import java.util.Date
import java.util.UUID

@Entity(tableName = "gastos")
data class GastoModel(
    @PrimaryKey
    override val id: String = UUID.randomUUID().toString(),
    override val fecha: Date = Date(),
    override val concepto: String,
    override val importe: Double,
    override val metodoPago: String,
    override val tipo: String = "gasto"
) : Entrada(id, fecha, concepto, importe, metodoPago, tipo)

fun GastoModel.toMap() : Map<String, Any?> = mapOf(
    "id" to id,
    "fecha" to fecha,
    "concepto" to concepto,
    "importe" to importe,
    "metodoPago" to metodoPago,
    "tipo" to tipo
)

fun DocumentSnapshot.toGastoModel() : GastoModel? {
    return try {
        GastoModel(
            id = getString("id") ?: return null,
            fecha = getDate("fecha") ?: Date(),
            concepto = getString("concepto") ?: "",
            importe = getDouble("importe") ?: 0.0,
            metodoPago = getString("metodoPago") ?: "",
            tipo = getString("tipo") ?: ""
        )
    } catch (e: Exception) { null }
}
