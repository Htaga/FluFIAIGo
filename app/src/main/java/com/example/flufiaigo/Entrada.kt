package com.example.flufiaigo

import java.util.Date

abstract class Entrada(
    open val id: String,
    open val fecha: Date,
    open val concepto: String,
    open val importe: Double,
    open val metodoPago: String
)
