package com.example.flufiaigo

data class IngresoModel(
    override val id: String,
    override val fecha: Long,
    override val descripcion: String,
    override val categoria: String,
    override val cantidad: Double,
    val conImpuestos: Boolean
) : Entrada(id, fecha, descripcion, categoria, cantidad)
