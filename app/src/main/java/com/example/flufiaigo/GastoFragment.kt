package com.example.flufiaigo

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.flufiaigo.databinding.FragmentGastoBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class GastoFragment : Fragment() {

    private val viewModel: GastoViewModel by viewModels()
    private var _binding: FragmentGastoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGastoBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val calendario = Calendar.getInstance()
        val formatoFecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        
        // 1. Configurar la fecha por defecto si está vacía
        if (viewModel.fechaTexto.value.isNullOrEmpty()) {
            viewModel.fechaReal.value = calendario.time
            viewModel.fechaTexto.value = formatoFecha.format(calendario.time)
        }

        // 2. Mostrar el DatePickerDialog al hacer clic
        binding.tvFechaGasto.setOnClickListener {
            // Sincronizar el calendario con la fecha seleccionada previamente
            viewModel.fechaReal.value?.let { date ->
                calendario.time = date
            }
            
            val anio = calendario.get(Calendar.YEAR)
            val mes = calendario.get(Calendar.MONTH)
            val dia = calendario.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, yearSelected, monthSelected, daySelected ->
                    calendario.set(yearSelected, monthSelected, daySelected)
                    viewModel.fechaReal.value = calendario.time
                    viewModel.fechaTexto.value = formatoFecha.format(calendario.time)
                },
                anio, mes, dia
            )
            datePickerDialog.show()
        }

        // 3. Observar la creación del gasto para guardarlo
        viewModel.gastoActual.observe(viewLifecycleOwner) { gasto ->
            if (gasto != null) {
                val dao = AppDatabase.getDatabase(requireContext()).financeDao()
                lifecycleScope.launch {
                    dao.insertGasto(gasto)
                    Toast.makeText(requireContext(), "Gasto guardado en Base de Datos", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}