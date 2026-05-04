package com.example.flufiaigo

import androidx.room.Entity
import androidx.room.PrimaryKey
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
