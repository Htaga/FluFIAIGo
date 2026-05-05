package com.example.flufiaigo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HomeFragment : Fragment() {

    private lateinit var adapter: MovimientoAdapter

    private var listaGastos: List<GastoModel> = emptyList()
    private var listaIngresos: List<IngresoModel> = emptyList()
    private var listaNominas: List<NominaModel> = emptyList()
    private var listaCompletaMovimientos: List<Entrada> = emptyList()

    // Variables del selector de meses a nivel de clase para poder usarlas en cualquier parte
    private val calendario = Calendar.getInstance()
    private val formatoMes = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
    private var tvMesActual: TextView? = null

    // 1. COMBINAMOS LOS DATOS DE LA BBDD
    private fun actualizarListaCombinada() {
        val combinada = mutableListOf<Entrada>()
        combinada.addAll(listaGastos)
        combinada.addAll(listaIngresos)

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
        combinada.sortByDescending { it.fecha }

        listaCompletaMovimientos = combinada
        actualizarMes()
    }

    // 2. FILTRAMOS POR MES Y SE LO PASAMOS AL ADAPTER
    private fun actualizarMes() {
        tvMesActual?.text = formatoMes.format(calendario.time).replaceFirstChar { it.uppercase() }

        val mesSeleccionado = calendario.get(Calendar.MONTH)
        val anioSeleccionado = calendario.get(Calendar.YEAR)

        val listaFiltrada = listaCompletaMovimientos.filter { movimiento ->
            val calMovimiento = Calendar.getInstance()
            calMovimiento.time = movimiento.fecha

            calMovimiento.get(Calendar.MONTH) == mesSeleccionado &&
                    calMovimiento.get(Calendar.YEAR) == anioSeleccionado
        }

        if (::adapter.isInitialized) {
            adapter.actualizarDatos(listaFiltrada)
        }
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

        val database = FluFiAIGoDatabase.getInstance(requireContext())
        val daoGasto = database.gastoDao
        val daoIngreso = database.ingresoDao
        val daoNomina = database.nominaDao

        // --- DESLIZAR PARA BORRAR ---
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val posicion = viewHolder.adapterPosition
                val movimientoBorrado = adapter.obtenerMovimiento(posicion)

                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                    when (movimientoBorrado) {
                        is GastoModel -> daoGasto.deleteGasto(movimientoBorrado)
                        is IngresoModel -> daoIngreso.deleteIngreso(movimientoBorrado)
                        is Entrada -> {
                             if (movimientoBorrado.tipo == "nomina") {
                                val nominaOriginal = listaNominas.find { it.id == movimientoBorrado.id }
                                if (nominaOriginal != null) {
                                    daoNomina.deleteNomina(nominaOriginal)
                                }
                            }
                        }
                    }
                }
                Toast.makeText(requireContext(), "Movimiento eliminado", Toast.LENGTH_SHORT).show()
            }
        }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        // --- SELECTOR DE MESES ---
        tvMesActual = view.findViewById(R.id.tvMesActual)
        val btnMesAnterior = view.findViewById<ImageButton>(R.id.btnMesAnterior)
        val btnMesSiguiente = view.findViewById<ImageButton>(R.id.btnMesSiguiente)

        btnMesAnterior.setOnClickListener {
            calendario.add(Calendar.MONTH, -1)
            actualizarMes()
        }

        btnMesSiguiente.setOnClickListener {
            calendario.add(Calendar.MONTH, 1)
            actualizarMes()
        }

        tvMesActual?.setOnClickListener {
            val datePicker = android.app.DatePickerDialog(
                requireContext(),
                { _, year, month, _ ->
                    calendario.set(Calendar.YEAR, year)
                    calendario.set(Calendar.MONTH, month)
                    actualizarMes()
                },
                calendario.get(Calendar.YEAR),
                calendario.get(Calendar.MONTH),
                calendario.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }

        // --- OBSERVERS DE LA BASE DE DATOS ---
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

        // --- NAVEGACIÓN Y BOTONES ---
        view.findViewById<Button>(R.id.btnNuevoGasto).setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_gastoFragment)
        }

        view.findViewById<Button>(R.id.btnNuevoIngreso).setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_ingresoFragment)
        }

        view.findViewById<Button>(R.id.btnNuevaNomina).setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_nominaFragment)
        }

        view.findViewById<Button>(R.id.btnSoporte).setOnClickListener {
            val urlSoporte = "https://github.com/Htaga/FluFIAIGo"
            val intentImplicito = android.content.Intent(android.content.Intent.ACTION_VIEW)
            intentImplicito.data = android.net.Uri.parse(urlSoporte)
            startActivity(intentImplicito)
        }
    }
}