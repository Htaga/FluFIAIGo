package com.example.flufiaigo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class IngresoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ingreso, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnGuardar = view.findViewById<Button>(R.id.btnGuardarIngreso)

        btnGuardar.setOnClickListener {
            // Simulamos que guarda mostrando un mensaje
            Toast.makeText(requireContext(), "Ingreso guardado", Toast.LENGTH_SHORT).show()

            // Volvemos a la pantalla principal (Home) sacando este fragmento de la pila
            findNavController().popBackStack()
        }
    }
}