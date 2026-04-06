package com.example.flufiaigo

import java.util.Date

data class NominaModel(    val id: String,
                          val empleadoNombre: String,
                          val sueldoBase: Double,
                          val deducciones: Double,
                          val bonificaciones: Double,
                          val fecha: Date,
                          val netoARecibir: Double
)
