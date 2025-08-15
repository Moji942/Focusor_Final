package com.focusor.presentation.education

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.focusor.databinding.FragmentEducationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EducationFragment : Fragment() {
    
    private var _binding: FragmentEducationBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: EducationViewModel by viewModels()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEducationBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        observeViewModel()
    }
    
    private fun setupUI() {
        binding.fabAddSubject.setOnClickListener {
            // TODO: Navigate to add subject screen
        }
    }
    
    private fun observeViewModel() {
        // TODO: Observe ViewModel state changes
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}