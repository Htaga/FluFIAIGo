package com.example.flufiaigo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.flufiaigo.databinding.FragmentNominaBinding

class NominaFragment : Fragment() {

    // Iniciamos el ViewModel
    private val viewModel: NominaViewModel by viewModels {
        val repository = (requireActivity().application as FluFIAIGoApplication).nominaRepository
        NominaViewModelFactory(repository)
    }

    // Variable para el DataBinding
    private var _binding: FragmentNominaBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflamos la vista usando DataBinding
        _binding = FragmentNominaBinding.inflate(inflater, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observar la creación de la nómina (ya guardada por el ViewModel)
        viewModel.nominaActual.observe(viewLifecycleOwner) { nomina ->
            if (nomina != null) {
                Toast.makeText(requireContext(), "Nómina guardada en Base de Datos", android.widget.Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
        }

        // Configuramos el botón de atrás de la barra superior
        binding.topAppBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Evitamos fugas de memoria
    }
}