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

    private var listaGastos: List<GastoModel> = emptyList()
    private var listaIngresos: List<IngresoModel> = emptyList()
    private var listaNominas: List<NominaModel> = emptyList()

    private fun actualizarListaCombinada() {
        val combinada = mutableListOf<Entrada>()
        combinada.addAll(listaGastos)
        combinada.addAll(listaIngresos)
        
        // Convertimos las nóminas a un objeto Entrada genérico para poder mostrarlas en la lista
        val nominasComoEntrada = listaNominas.map { nomina ->
            Entrada(
                id = nomina.id,
                fecha = nomina.fecha,
                concepto = "Nómina: ${nomina.empleadoNombre}",
                importe = nomina.salarioNeto,
                metodoPago = "-",
                tipo = "nomina"
            )
        }
        combinada.addAll(nominasComoEntrada)
        
        // Ordenamos todas las transacciones por fecha descendente (más reciente primero)
        combinada.sortByDescending { it.fecha }
        
        adapter.actualizarDatos(combinada)
    }

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

        daoGasto.getAllGastos().observe(viewLifecycleOwner) { lista ->
            listaGastos = lista
            actualizarListaCombinada()
        }

        daoIngreso.getAllIngresos().observe(viewLifecycleOwner) { lista ->
            listaIngresos = lista
            actualizarListaCombinada()
        }

        daoNomina.getAllNominas().observe(viewLifecycleOwner) { lista ->
            listaNominas = lista
            actualizarListaCombinada()
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