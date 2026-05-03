package com.example.flufiaigo

data class GastoModel(
    override val id: String,
    override val fecha: Long,
    override val descripcion: String,
    override val categoria: String,
    override val cantidad: Double,
    val tipoPago: String
) : Entrada(id, fecha, descripcion, categoria, cantidad)
