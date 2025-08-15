package com.focusor.app.presentation.screens.education

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.focusor.app.domain.model.Subject
import com.focusor.app.domain.repository.EducationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EducationViewModel @Inject constructor(
    private val educationRepository: EducationRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<EducationUiState>(EducationUiState.Loading)
    val uiState: StateFlow<EducationUiState> = _uiState.asStateFlow()
    
    init {
        loadSubjects()
    }
    
    fun loadSubjects() {
        viewModelScope.launch {
            _uiState.value = EducationUiState.Loading
            
            try {
                educationRepository.getAllActiveSubjects()
                    .catch { error ->
                        _uiState.value = EducationUiState.Error(error.message ?: "Unknown error")
                    }
                    .collect { subjects ->
                        _uiState.value = EducationUiState.Success(subjects)
                    }
            } catch (error: Exception) {
                _uiState.value = EducationUiState.Error(error.message ?: "Unknown error")
            }
        }
    }
    
    fun addSubject(subject: Subject) {
        viewModelScope.launch {
            try {
                educationRepository.insertSubject(subject)
                // The UI will automatically update through the Flow
            } catch (error: Exception) {
                // Handle error
            }
        }
    }
    
    fun updateSubjectProgress(subjectId: Long, currentPage: Int) {
        viewModelScope.launch {
            try {
                educationRepository.updateCurrentPage(subjectId, currentPage)
                // The UI will automatically update through the Flow
            } catch (error: Exception) {
                // Handle error
            }
        }
    }
    
    fun onSubjectClick(subject: Subject) {
        // Navigate to subject detail screen
        // This would be handled by navigation
    }
    
    fun deleteSubject(subject: Subject) {
        viewModelScope.launch {
            try {
                educationRepository.deleteSubject(subject)
                // The UI will automatically update through the Flow
            } catch (error: Exception) {
                // Handle error
            }
        }
    }
}

sealed class EducationUiState {
    object Loading : EducationUiState()
    data class Success(val subjects: List<Subject>) : EducationUiState()
    data class Error(val message: String) : EducationUiState()
}