package com.focusor.app.domain.model

import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class StudyPlan(
    val subjects: List<Subject>,
    val totalAvailableHours: Double = 24.0,
    val targetCompletionDate: LocalDate? = null
) {
    
    val totalRemainingPages: Int
        get() = subjects.sumOf { it.remainingPages }
    
    val totalHoursNeeded: Double
        get() = subjects.sumOf { it.hoursNeededToComplete }
    
    val averageReadingSpeed: Double
        get() = subjects.filter { it.readingSpeed > 0 }.let { 
            if (it.isEmpty()) 0.0 else it.sumOf { subject -> subject.readingSpeed } / it.size 
        }
    
    val subjectsByPriority: List<Subject>
        get() = subjects.sortedByDescending { it.priority }
    
    val overdueSubjects: List<Subject>
        get() = subjects.filter { it.isOverdue }
    
    val onTrackSubjects: List<Subject>
        get() = subjects.filter { it.isOnTrack }
    
    val criticalSubjects: List<Subject>
        get() = subjects.filter { it.daysUntilTarget <= 7 && it.remainingPages > 0 }
    
    fun calculateOptimalHoursDistribution(): Map<Long, Double> {
        if (subjects.isEmpty()) return emptyMap()
        
        val distribution = mutableMapOf<Long, Double>()
        val totalPriority = subjects.sumOf { it.priority.toDouble() }
        
        subjects.forEach { subject ->
            val priorityWeight = subject.priority / totalPriority
            val hoursAllocated = totalAvailableHours * priorityWeight
            distribution[subject.id] = hoursAllocated
        }
        
        return distribution
    }
    
    fun calculateMultiSubjectCompletionDate(): LocalDate? {
        if (subjects.isEmpty() || averageReadingSpeed <= 0) return null
        
        val totalPages = totalRemainingPages
        val totalHoursNeeded = totalPages / averageReadingSpeed
        val daysNeeded = (totalHoursNeeded / (totalAvailableHours * 0.8)).toLong() // 80% efficiency
        
        return LocalDate.now().plusDays(daysNeeded)
    }
    
    fun getRecommendedStudyOrder(): List<Subject> {
        return subjects.sortedWith(
            compareBy<Subject> { it.isOverdue }.reversed()
                .thenBy { it.daysUntilTarget }
                .thenByDescending { it.priority }
        )
    }
    
    fun calculateDailyStudyTargets(): Map<Long, Int> {
        val targets = mutableMapOf<Long, Int>()
        val distribution = calculateOptimalHoursDistribution()
        
        subjects.forEach { subject ->
            val hoursAllocated = distribution[subject.id] ?: 0.0
            val pagesTarget = (hoursAllocated * subject.readingSpeed).toInt()
            targets[subject.id] = pagesTarget.coerceAtLeast(1)
        }
        
        return targets
    }
    
    fun getStudyRecommendations(): List<StudyRecommendation> {
        val recommendations = mutableListOf<StudyRecommendation>()
        
        overdueSubjects.forEach { subject ->
            recommendations.add(
                StudyRecommendation(
                    subjectId = subject.id,
                    subjectName = subject.name,
                    type = StudyRecommendationType.OVERDUE,
                    message = "Subject is overdue. Increase daily study time.",
                    suggestedHours = subject.hoursNeededToComplete / 7.0 // Complete in a week
                )
            )
        }
        
        criticalSubjects.forEach { subject ->
            recommendations.add(
                StudyRecommendation(
                    subjectId = subject.id,
                    subjectName = subject.name,
                    type = StudyRecommendationType.CRITICAL,
                    message = "Subject due within 7 days. Prioritize study time.",
                    suggestedHours = subject.hoursNeededToComplete / subject.daysUntilTarget.toDouble()
                )
            )
        }
        
        return recommendations
    }
}

data class StudyRecommendation(
    val subjectId: Long,
    val subjectName: String,
    val type: StudyRecommendationType,
    val message: String,
    val suggestedHours: Double
)

enum class StudyRecommendationType {
    OVERDUE, CRITICAL, OPTIMIZATION, REMINDER
}