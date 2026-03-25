package com.example.flufiaigo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.flufiaigo.databinding.FragmentNominaBinding

class NominaFragment : Fragment() {

    // Iniciamos el ViewModel
    private val viewModel: NominaViewModel by viewModels()

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Evitamos fugas de memoria
    }
}