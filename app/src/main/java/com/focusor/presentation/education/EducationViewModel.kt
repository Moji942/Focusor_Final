package com.focusor.presentation.education

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EducationViewModel @Inject constructor() : ViewModel() {
    
    private val _uiState = MutableStateFlow(EducationUiState())
    val uiState: StateFlow<EducationUiState> = _uiState.asStateFlow()
    
    init {
        loadSubjects()
    }
    
    private fun loadSubjects() {
        viewModelScope.launch {
            // TODO: Load subjects from repository
            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }
}

data class EducationUiState(
    val isLoading: Boolean = true,
    val subjects: List<Any> = emptyList(),
    val error: String? = null
)