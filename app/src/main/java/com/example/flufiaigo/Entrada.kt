package com.example.flufiaigo

abstract class Entrada(
    open val id: String,
    open val fecha: Long,
    open val descripcion: String,
    open val categoria: String,
    open val cantidad: Double
)