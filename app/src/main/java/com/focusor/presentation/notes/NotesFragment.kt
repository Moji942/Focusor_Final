package com.focusor.presentation.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import com.focusor.databinding.FragmentSimpleBinding
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@AndroidEntryPoint
class NotesFragment : Fragment() {
    private var _binding: FragmentSimpleBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NotesViewModel by viewModels()
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSimpleBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textTitle.text = "Notes"
        binding.textDescription.text = "Create and organize your notes with categories and reminders"
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

@HiltViewModel
class NotesViewModel @Inject constructor() : ViewModel()