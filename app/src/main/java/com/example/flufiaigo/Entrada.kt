package com.example.flufiaigo

import java.util.Date

abstract class Entrada(
    open val id: String,
    open val fecha: Date,
    open val descripcion: String,
    open val categoria: String,
    open val cantidad: Double
)
