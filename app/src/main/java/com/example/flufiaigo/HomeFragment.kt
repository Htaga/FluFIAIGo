package com.example.flufiaigo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeFragment : Fragment() {

    private lateinit var adapter: MovimientoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewEntries)
        adapter = MovimientoAdapter()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        val daoGasto = FluFiAIGoDatabase.getInstance(requireContext()).gastoDao
        val daoIngreso = FluFiAIGoDatabase.getInstance(requireContext()).ingresoDao
        val daoNomina = FluFiAIGoDatabase.getInstance(requireContext()).nominaDao

        daoGasto.getAllGastos().observe(viewLifecycleOwner) { listaMovimientos ->
            // Cuando añades un gasto, ROOM avisa aquí automáticamente
            adapter.actualizarDatos(listaMovimientos)
        }

        val btnGasto = view.findViewById<Button>(R.id.btnNuevoGasto)
        val btnIngreso = view.findViewById<Button>(R.id.btnNuevoIngreso)
        val btnNomina = view.findViewById<Button>(R.id.btnNuevaNomina)

        btnGasto.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_gastoFragment)
        }

        btnIngreso.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_ingresoFragment)
        }

        btnNomina.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_nominaFragment)
        }

        val btnSoporte = view.findViewById<Button>(R.id.btnSoporte)

        btnSoporte.setOnClickListener {
            // Creamos un Intent Implícito para abrir una URL
            val urlSoporte = "https://github.com/Htaga/FluFIAIGo"
            val intentImplicito = android.content.Intent(android.content.Intent.ACTION_VIEW)
            intentImplicito.data = android.net.Uri.parse(urlSoporte)

            // Iniciamos la actividad (Android preguntará con qué navegador abrirlo)
            startActivity(intentImplicito)
        }
    }
}