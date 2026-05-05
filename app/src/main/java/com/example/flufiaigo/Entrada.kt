package com.example.flufiaigo

import java.util.Date

open class Entrada(
    open val id: String,
    open val fecha: Date,
    open val concepto: String,
    open val importe: Double,
    open val metodoPago: String,
    open val tipo: String
)
