package com.example.flufiaigo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnMovimiento = view.findViewById<Button>(R.id.btnNuevoMovimiento)
        val btnNomina = view.findViewById<Button>(R.id.btnNuevaNomina)

        btnMovimiento.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_movimientoFragment)
        }

        btnNomina.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_nominaFragment)
        }
    }
}