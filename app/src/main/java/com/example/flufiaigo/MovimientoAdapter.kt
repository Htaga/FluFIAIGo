package com.example.flufiaigo

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Locale

class MovimientoAdapter(private var movimientos: List<Entrada> = emptyList()) :
    RecyclerView.Adapter<MovimientoAdapter.ViewHolder>() {

    // "Engancha" los elementos del XML de la tarjeta
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvConcepto: TextView = view.findViewById(R.id.tvConcepto)
        val tvFecha: TextView = view.findViewById(R.id.tvFecha)
        val tvImporte: TextView = view.findViewById(R.id.tvImporte)
        val ivIcono: ImageView = view.findViewById(R.id.ivIcono)
    }

    // Infla la tarjeta visual
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_finance_entry, parent, false)
        return ViewHolder(view)
    }

    // Rellena la tarjeta con los datos de la base de datos
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movimiento = movimientos[position]
        val formatoFecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        holder.tvConcepto.text = movimiento.concepto
        holder.tvFecha.text = formatoFecha.format(movimiento.fecha)

        if (movimiento.tipo == "ingreso") {
            holder.tvImporte.text = "+ ${movimiento.importe} €"
            holder.tvImporte.setTextColor(Color.parseColor("#4CAF50")) // Verde
            holder.ivIcono.setImageResource(android.R.drawable.ic_input_add)
        } else if (movimiento.tipo == "gasto") {
            holder.tvImporte.text = "- ${movimiento.importe} €"
            holder.tvImporte.setTextColor(Color.parseColor("#F44336")) // Rojo
            holder.ivIcono.setImageResource(android.R.drawable.ic_menu_delete)
        } else if (movimiento.tipo == "nomina") {
            holder.tvImporte.text = "${movimiento.importe} €"
            holder.tvImporte.setTextColor(Color.parseColor("#2196F3")) // Azul
            holder.ivIcono.setImageResource(android.R.drawable.ic_menu_myplaces) // Icono genérico
        }
    }

    override fun getItemCount() = movimientos.size

    fun actualizarDatos(nuevosMovimientos: List<Entrada>) {
        this.movimientos = nuevosMovimientos
        notifyDataSetChanged()
    }

    fun obtenerMovimiento(posicion: Int): Entrada {
        return movimientos[posicion]
    }
}