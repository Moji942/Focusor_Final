package com.focusor.studyplanner.domain.usecase.education

import com.focusor.studyplanner.data.entity.StudySessionEntity
import com.focusor.studyplanner.data.entity.SubjectEntity
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.roundToInt

class StudyCalculationEngine @Inject constructor() {
    
    /**
     * Calculate daily pages requirement based on remaining pages and days
     */
    fun calculateDailyPagesRequired(
        totalPages: Int,
        completedPages: Int,
        targetDate: LocalDate,
        currentDate: LocalDate = LocalDate.now()
    ): Double {
        val remainingPages = totalPages - completedPages
        val daysRemaining = ChronoUnit.DAYS.between(currentDate, targetDate).toInt()
        
        if (daysRemaining <= 0) return remainingPages.toDouble()
        return remainingPages.toDouble() / daysRemaining
    }
    
    /**
     * Calculate reading speed from a study session
     */
    fun calculateReadingSpeed(pagesRead: Int, hoursSpent: Double): Double {
        if (hoursSpent <= 0) return 0.0
        return pagesRead / hoursSpent
    }
    
    /**
     * Calculate weighted average reading speed from recent sessions
     * More recent sessions have higher weight
     */
    fun calculateWeightedAverageSpeed(sessions: List<StudySessionEntity>): Double {
        if (sessions.isEmpty()) return 0.0
        
        val sortedSessions = sessions.sortedByDescending { it.date }
        var totalWeightedSpeed = 0.0
        var totalWeight = 0.0
        
        sortedSessions.take(7).forEachIndexed { index, session ->
            val speed = calculateReadingSpeed(session.actualPages, session.actualHours)
            val weight = 1.0 / (index + 1) // More recent sessions have higher weight
            totalWeightedSpeed += speed * weight
            totalWeight += weight
        }
        
        return if (totalWeight > 0) totalWeightedSpeed / totalWeight else 0.0
    }
    
    /**
     * Calculate hours needed today based on pages to read and reading speed
     */
    fun calculateHoursNeededToday(
        pagesToRead: Double,
        readingSpeed: Double
    ): Double {
        if (readingSpeed <= 0) return 0.0
        return pagesToRead / readingSpeed
    }
    
    /**
     * Calculate projected completion date based on current pace
     */
    fun calculateProjectedCompletionDate(
        remainingPages: Int,
        dailyAveragePages: Double,
        currentDate: LocalDate = LocalDate.now()
    ): LocalDate {
        if (dailyAveragePages <= 0) return currentDate.plusYears(10) // Far future date
        val daysNeeded = ceil(remainingPages / dailyAveragePages).toLong()
        return currentDate.plusDays(daysNeeded)
    }
    
    /**
     * Calculate if subject is on track to meet deadline
     */
    fun isOnTrack(
        subject: SubjectEntity,
        currentDate: LocalDate = LocalDate.now()
    ): Boolean {
        val dailyRequired = calculateDailyPagesRequired(
            subject.totalPages,
            subject.completedPages,
            subject.targetDate,
            currentDate
        )
        
        val hoursPerDay = 8.0 // Reasonable study hours per day
        val maxPagesPerDay = subject.currentReadingSpeed * hoursPerDay
        
        return maxPagesPerDay >= dailyRequired
    }
    
    /**
     * Calculate catch-up pages if behind schedule
     */
    fun calculateCatchUpPages(
        plannedPagesToday: Int,
        actualPagesYesterday: Int,
        plannedPagesYesterday: Int,
        daysRemaining: Int
    ): Int {
        val deficit = plannedPagesYesterday - actualPagesYesterday
        if (deficit <= 0) return plannedPagesToday
        
        val additionalPagesPerDay = ceil(deficit.toDouble() / max(daysRemaining, 1)).toInt()
        return plannedPagesToday + additionalPagesPerDay
    }
    
    /**
     * Distribute available study hours across multiple subjects based on priority and urgency
     */
    fun distributeStudyHours(
        subjects: List<SubjectEntity>,
        availableHours: Double,
        currentDate: LocalDate = LocalDate.now()
    ): Map<Long, Double> {
        if (subjects.isEmpty() || availableHours <= 0) return emptyMap()
        
        // Calculate urgency score for each subject
        val subjectScores = subjects.map { subject ->
            val daysRemaining = ChronoUnit.DAYS.between(currentDate, subject.targetDate).toDouble()
            val remainingPages = subject.totalPages - subject.completedPages
            val urgency = if (daysRemaining > 0) remainingPages / daysRemaining else Double.MAX_VALUE
            val score = subject.priority * urgency
            subject.id to score
        }.toMap()
        
        val totalScore = subjectScores.values.sum()
        if (totalScore <= 0) return emptyMap()
        
        // Distribute hours proportionally
        return subjectScores.mapValues { (_, score) ->
            (score / totalScore) * availableHours
        }
    }
    
    /**
     * Calculate study efficiency score (0-100)
     */
    fun calculateEfficiencyScore(
        plannedPages: Int,
        actualPages: Int,
        plannedHours: Double,
        actualHours: Double
    ): Int {
        if (plannedPages <= 0 || plannedHours <= 0) return 0
        
        val pageEfficiency = (actualPages.toDouble() / plannedPages) * 50
        val timeEfficiency = if (actualHours > 0) {
            val expectedSpeed = plannedPages / plannedHours
            val actualSpeed = actualPages / actualHours
            (actualSpeed / expectedSpeed) * 50
        } else 0.0
        
        return (pageEfficiency + timeEfficiency).coerceIn(0.0, 100.0).roundToInt()
    }
    
    /**
     * Generate smart recommendations based on study patterns
     */
    fun generateRecommendations(
        subject: SubjectEntity,
        recentSessions: List<StudySessionEntity>,
        currentDate: LocalDate = LocalDate.now()
    ): List<String> {
        val recommendations = mutableListOf<String>()
        
        val remainingPages = subject.totalPages - subject.completedPages
        val daysRemaining = ChronoUnit.DAYS.between(currentDate, subject.targetDate).toInt()
        val dailyRequired = calculateDailyPagesRequired(
            subject.totalPages,
            subject.completedPages,
            subject.targetDate,
            currentDate
        )
        
        // Check if behind schedule
        if (!isOnTrack(subject, currentDate)) {
            val additionalPages = (dailyRequired - subject.currentReadingSpeed * 4).roundToInt()
            if (additionalPages > 0) {
                recommendations.add("You need to read $additionalPages extra pages daily to catch up")
            }
            
            val additionalHours = calculateHoursNeededToday(dailyRequired, subject.currentReadingSpeed) - 4
            if (additionalHours > 0) {
                val minutes = (additionalHours * 60).roundToInt()
                recommendations.add("Add $minutes minutes to your daily study time")
            }
        }
        
        // Check reading speed improvement
        if (recentSessions.size >= 3) {
            val oldSpeed = calculateReadingSpeed(
                recentSessions.takeLast(3).first().actualPages,
                recentSessions.takeLast(3).first().actualHours
            )
            val newSpeed = calculateReadingSpeed(
                recentSessions.first().actualPages,
                recentSessions.first().actualHours
            )
            
            val improvement = ((newSpeed - oldSpeed) / oldSpeed * 100).roundToInt()
            if (improvement > 0) {
                recommendations.add("Your reading speed improved by $improvement% this week!")
            } else if (improvement < -10) {
                recommendations.add("Your reading speed decreased. Consider taking more breaks")
            }
        }
        
        // Project completion
        val projectedDate = calculateProjectedCompletionDate(
            remainingPages,
            subject.currentReadingSpeed * 4, // Assuming 4 hours daily
            currentDate
        )
        
        val daysLate = ChronoUnit.DAYS.between(subject.targetDate, projectedDate).toInt()
        if (daysLate > 0) {
            recommendations.add("At current pace, you'll finish $daysLate days late")
            val requiredSpeed = remainingPages.toDouble() / (daysRemaining * 4)
            val speedIncrease = ((requiredSpeed - subject.currentReadingSpeed) / subject.currentReadingSpeed * 100).roundToInt()
            if (speedIncrease > 0) {
                recommendations.add("Increase reading speed by $speedIncrease% to finish on time")
            }
        } else if (daysLate < -3) {
            recommendations.add("Great! You're ahead of schedule by ${-daysLate} days")
        }
        
        return recommendations
    }
    
    /**
     * Detect scheduling conflicts
     */
    fun detectConflicts(
        subjects: List<SubjectEntity>,
        availableHoursPerDay: Double,
        currentDate: LocalDate = LocalDate.now()
    ): List<String> {
        val conflicts = mutableListOf<String>()
        
        val totalHoursNeeded = subjects.sumOf { subject ->
            val dailyPages = calculateDailyPagesRequired(
                subject.totalPages,
                subject.completedPages,
                subject.targetDate,
                currentDate
            )
            calculateHoursNeededToday(dailyPages, subject.currentReadingSpeed.coerceAtLeast(10.0))
        }
        
        if (totalHoursNeeded > availableHoursPerDay) {
            val deficit = totalHoursNeeded - availableHoursPerDay
            conflicts.add("You need ${deficit.roundToInt()} more hours daily for all subjects")
            
            // Find subjects with closest deadlines
            val urgentSubjects = subjects
                .sortedBy { it.targetDate }
                .take(2)
                .joinToString(", ") { it.name }
            
            conflicts.add("Prioritize: $urgentSubjects")
        }
        
        // Check for same-day deadlines
        val deadlineGroups = subjects.groupBy { it.targetDate }
        deadlineGroups.forEach { (date, subjectsOnDate) ->
            if (subjectsOnDate.size > 1) {
                val names = subjectsOnDate.joinToString(", ") { it.name }
                val daysUntil = ChronoUnit.DAYS.between(currentDate, date)
                conflicts.add("$names have the same deadline in $daysUntil days")
            }
        }
        
        return conflicts
    }
}