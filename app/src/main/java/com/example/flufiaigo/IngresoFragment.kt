package com.example.flufiaigo

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.flufiaigo.databinding.FragmentIngresoBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class IngresoFragment : Fragment() {

    private val viewModel: IngresoViewModel by viewModels {
        val repository = (activity?.application as FluFIAIGoApplication).ingresoRepository
        IngresoViewModelFactory(repository)
    }
    private var _binding: FragmentIngresoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIngresoBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val calendario = Calendar.getInstance()
        val formatoFecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        
        if (viewModel.fechaTexto.value.isNullOrEmpty()) {
            viewModel.fechaReal.value = calendario.time
            viewModel.fechaTexto.value = formatoFecha.format(calendario.time)
        }

        binding.tvFechaIngreso.setOnClickListener {
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

        viewModel.ingresoActual.observe(viewLifecycleOwner) { ingreso ->
            if (ingreso != null) {
                Toast.makeText(requireContext(), "Ingreso guardado en Base de Datos", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}