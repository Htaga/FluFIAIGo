package com.example.flufiaigo

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity(tableName = "gastos")
data class GastoModel(
    @PrimaryKey
    override val id: String = UUID.randomUUID().toString(),
    override val fecha: Date = Date(),
    override val descripcion: String,
    override val categoria: String,
    override val cantidad: Double,
    val tipoPago: String
) : Entrada(id, fecha, descripcion, categoria, cantidad)
