package com.focusor.studyplanner.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.focusor.studyplanner.data.entity.StudySessionEntity
import com.focusor.studyplanner.data.entity.SubjectEntity
import com.focusor.studyplanner.domain.repository.EducationRepository
import com.focusor.studyplanner.domain.usecase.education.StudyCalculationEngine
import com.focusor.studyplanner.presentation.ui.education.SessionInput
import com.focusor.studyplanner.presentation.ui.education.SubjectData
import com.focusor.studyplanner.presentation.ui.education.SubjectInput
import com.focusor.studyplanner.presentation.ui.education.TodayProgress
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class EducationViewModel @Inject constructor(
    private val repository: EducationRepository,
    private val calculationEngine: StudyCalculationEngine
) : ViewModel() {
    
    private val _subjects = MutableStateFlow<List<SubjectData>>(emptyList())
    val subjects: StateFlow<List<SubjectData>> = _subjects.asStateFlow()
    
    private val _todayProgress = MutableStateFlow(TodayProgress())
    val todayProgress: StateFlow<TodayProgress> = _todayProgress.asStateFlow()
    
    private val _recommendations = MutableStateFlow<List<String>>(emptyList())
    val recommendations: StateFlow<List<String>> = _recommendations.asStateFlow()
    
    private val _conflicts = MutableStateFlow<List<String>>(emptyList())
    val conflicts: StateFlow<List<String>> = _conflicts.asStateFlow()
    
    private var selectedSubject: SubjectData? = null
    
    init {
        loadSubjects()
        loadTodayProgress()
    }
    
    private fun loadSubjects() {
        viewModelScope.launch {
            repository.getAllActiveSubjects().collect { subjectEntities ->
                val subjectDataList = subjectEntities.map { entity ->
                    val daysRemaining = ChronoUnit.DAYS.between(
                        LocalDate.now(),
                        entity.targetDate
                    ).toInt()
                    
                    val dailyRequired = calculationEngine.calculateDailyPagesRequired(
                        entity.totalPages,
                        entity.completedPages,
                        entity.targetDate
                    )
                    
                    SubjectData(
                        id = entity.id,
                        name = entity.name,
                        resourceType = entity.resourceType,
                        totalPages = entity.totalPages,
                        completedPages = entity.completedPages,
                        priority = entity.priority,
                        dailyPagesRequired = dailyRequired.toInt(),
                        currentReadingSpeed = entity.currentReadingSpeed,
                        daysRemaining = daysRemaining,
                        isOnTrack = calculationEngine.isOnTrack(entity)
                    )
                }
                
                _subjects.value = subjectDataList
                
                // Generate recommendations and detect conflicts
                if (subjectDataList.isNotEmpty()) {
                    generateRecommendations(subjectEntities)
                    detectConflicts(subjectEntities)
                }
            }
        }
    }
    
    private fun loadTodayProgress() {
        viewModelScope.launch {
            repository.getSessionsByDate(LocalDate.now()).collect { sessions ->
                val totalPages = sessions.sumOf { it.actualPages }
                val totalHours = sessions.sumOf { it.actualHours }
                
                // Calculate target for today
                val targetPages = _subjects.value.sumOf { it.dailyPagesRequired }
                
                _todayProgress.value = TodayProgress(
                    pagesCompleted = totalPages,
                    pagesTarget = targetPages.coerceAtLeast(1),
                    hoursStudied = totalHours
                )
            }
        }
    }
    
    private suspend fun generateRecommendations(subjects: List<SubjectEntity>) {
        val allRecommendations = mutableListOf<String>()
        
        subjects.forEach { subject ->
            val recentSessions = repository.getSessionsBySubject(subject.id)
                .first()
                .take(7)
            
            val recommendations = calculationEngine.generateRecommendations(
                subject,
                recentSessions,
                LocalDate.now()
            )
            
            allRecommendations.addAll(recommendations.take(2))
        }
        
        _recommendations.value = allRecommendations.take(5)
    }
    
    private fun detectConflicts(subjects: List<SubjectEntity>) {
        val conflicts = calculationEngine.detectConflicts(
            subjects,
            8.0, // Available hours per day
            LocalDate.now()
        )
        
        _conflicts.value = conflicts
    }
    
    suspend fun addSubject(input: SubjectInput) {
        val entity = SubjectEntity(
            name = input.name,
            resourceType = input.resourceType,
            totalPages = input.totalPages,
            completedPages = 0,
            startDate = LocalDate.now(),
            targetDate = LocalDate.parse(input.targetDate),
            priority = input.priority,
            isActive = true,
            currentReadingSpeed = 10.0, // Default reading speed
            averageReadingSpeed = 10.0
        )
        
        repository.insertSubject(entity)
    }
    
    suspend fun addStudySession(input: SessionInput) {
        val session = StudySessionEntity(
            subjectId = input.subjectId,
            date = LocalDate.now(),
            startTime = LocalTime.now().minusHours(input.hoursSpent.toLong()),
            endTime = LocalTime.now(),
            plannedPages = input.pagesRead,
            actualPages = input.pagesRead,
            plannedHours = input.hoursSpent,
            actualHours = input.hoursSpent,
            qualityRating = input.quality,
            notes = input.notes
        )
        
        repository.insertStudySession(session)
        
        // Update subject progress
        val subject = repository.getSubjectById(input.subjectId)
        subject?.let {
            val newCompletedPages = it.completedPages + input.pagesRead
            repository.updateProgress(input.subjectId, newCompletedPages)
            
            // Update reading speed
            val speed = calculationEngine.calculateReadingSpeed(
                input.pagesRead,
                input.hoursSpent
            )
            
            val recentSessions = repository.getSessionsBySubject(input.subjectId)
                .first()
            
            val avgSpeed = calculationEngine.calculateWeightedAverageSpeed(recentSessions)
            
            repository.updateReadingSpeed(input.subjectId, speed, avgSpeed)
        }
    }
    
    suspend fun updateProgress(subjectId: Long, additionalPages: Int) {
        val subject = repository.getSubjectById(subjectId)
        subject?.let {
            val newCompletedPages = it.completedPages + additionalPages
            repository.updateProgress(subjectId, newCompletedPages)
        }
    }
    
    fun selectSubject(subject: SubjectData) {
        selectedSubject = subject
    }
}