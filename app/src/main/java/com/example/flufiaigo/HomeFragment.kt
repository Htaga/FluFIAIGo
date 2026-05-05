package com.example.flufiaigo

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
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

    private val calendario = Calendar.getInstance()
    private val formatoMes = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
    private var tvMesActual: TextView? = null

    private fun actualizarListaCombinada() {
        val combinada = mutableListOf<Entrada>()
        combinada.addAll(listaGastos)
        combinada.addAll(listaIngresos)

        val nominasComoEntrada = listaNominas.map { nomina ->
            Entrada(
                id = nomina.id,
                fecha = nomina.fecha,
                concepto = getString(R.string.prefijo_nomina, nomina.empleadoNombre),
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // --- PREFERENCIAS Y MODO OSCURO ---
        val sharedPrefs = requireContext().getSharedPreferences("AjustesFluFi", Context.MODE_PRIVATE)

        // --- TOOLBAR Y MENÚ ---
        val toolbar = view.findViewById<MaterialToolbar>(R.id.topAppBar)
        val esOscuroInicial = sharedPrefs.getBoolean("modo_oscuro", false)
        val itemTema = toolbar.menu.findItem(R.id.action_theme)

        itemTema?.title = getString(R.string.cambiar_tema)
        itemTema?.setIcon(if (esOscuroInicial) R.drawable.baseline_wb_sunny_24 else R.drawable.baseline_dark_mode_24)

        toolbar.setOnMenuItemClickListener { menuItem ->
            if (menuItem.itemId == R.id.action_theme) {
                val esOscuro = sharedPrefs.getBoolean("modo_oscuro", false)
                val nuevoEstado = !esOscuro

                sharedPrefs.edit().putBoolean("modo_oscuro", nuevoEstado).apply()

                if (nuevoEstado) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    menuItem.setIcon(R.drawable.baseline_wb_sunny_24)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    menuItem.setIcon(R.drawable.baseline_dark_mode_24)
                }
                true
            } else false
        }

        // --- RECYCLERVIEW ---
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewEntries)
        adapter = MovimientoAdapter()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        val database = FluFiAIGoDatabase.getInstance(requireContext())
        val daoGasto = database.gastoDao
        val daoIngreso = database.ingresoDao
        val daoNomina = database.nominaDao

        // FORZAR SINCRONIZACIÓN CON FIREBASE AL ABRIR LA APP
        val repoGasto = GastoRepository(daoGasto)
        val repoIngreso = IngresoRepository(daoIngreso)
        val repoNomina = NominaRepository(daoNomina)
        repoGasto.syncFromFirestore(viewLifecycleOwner.lifecycleScope)
        repoIngreso.syncFromFirestore(viewLifecycleOwner.lifecycleScope)
        repoNomina.syncFromFirestore(viewLifecycleOwner.lifecycleScope)

        // --- SWIPE TO DELETE ---
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
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
                                val original = listaNominas.find { it.id == movimientoBorrado.id }
                                original?.let { daoNomina.deleteNomina(it) }
                            }
                        }
                    }
                }
                Toast.makeText(requireContext(), getString(R.string.movimiento_eliminado), Toast.LENGTH_SHORT).show()
            }
        }
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView)

        // --- SELECTOR DE MESES ---
        tvMesActual = view.findViewById(R.id.tvMesActual)
        val btnAnt = view.findViewById<ImageButton>(R.id.btnMesAnterior)
        val btnSig = view.findViewById<ImageButton>(R.id.btnMesSiguiente)

        btnAnt.setOnClickListener { calendario.add(Calendar.MONTH, -1); actualizarMes() }
        btnSig.setOnClickListener { calendario.add(Calendar.MONTH, 1); actualizarMes() }
        tvMesActual?.setOnClickListener {
            android.app.DatePickerDialog(requireContext(), { _, y, m, _ ->
                calendario.set(Calendar.YEAR, y); calendario.set(Calendar.MONTH, m); actualizarMes()
            }, calendario.get(Calendar.YEAR), calendario.get(Calendar.MONTH), 1).show()
        }

        // --- OBSERVERS ---
        daoGasto.getAllGastos().observe(viewLifecycleOwner) { lista ->
            listaGastos = lista; actualizarListaCombinada()
        }
        daoIngreso.getAllIngresos().observe(viewLifecycleOwner) { lista ->
            listaIngresos = lista; actualizarListaCombinada()
        }
        daoNomina.getAllNominas().observe(viewLifecycleOwner) { lista ->
            listaNominas = lista; actualizarListaCombinada()
        }

        // --- BOTONES NAVEGACIÓN ---
        view.findViewById<Button>(R.id.btnNuevoGasto).setOnClickListener { findNavController().navigate(R.id.action_homeFragment_to_gastoFragment) }
        view.findViewById<Button>(R.id.btnNuevoIngreso).setOnClickListener { findNavController().navigate(R.id.action_homeFragment_to_ingresoFragment) }
        view.findViewById<Button>(R.id.btnNuevaNomina).setOnClickListener { findNavController().navigate(R.id.action_homeFragment_to_nominaFragment) }
        view.findViewById<Button>(R.id.btnSoporte).setOnClickListener {
            startActivity(android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse("https://github.com/Htaga/FluFIAIGo")))
        }
    }
}