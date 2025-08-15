package com.focusor.app.domain.model

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

data class Subject(
    val id: Long = 0,
    val name: String,
    val source: String,
    val totalPages: Int,
    val targetDate: LocalDate,
    val currentPage: Int = 0,
    val dailyPages: Int = 0,
    val readingSpeed: Double = 0.0, // pages per hour
    val priority: Int = 1, // 1-5 scale
    val isActive: Boolean = true,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
) {
    
    val remainingPages: Int
        get() = (totalPages - currentPage).coerceAtLeast(0)
    
    val progressPercentage: Double
        get() = if (totalPages > 0) (currentPage.toDouble() / totalPages) * 100 else 0.0
    
    val daysUntilTarget: Long
        get() = ChronoUnit.DAYS.between(LocalDate.now(), targetDate)
    
    val isOverdue: Boolean
        get() = targetDate.isBefore(LocalDate.now()) && remainingPages > 0
    
    val estimatedCompletionDate: LocalDate?
        get() {
            if (readingSpeed <= 0 || dailyPages <= 0) return null
            val hoursNeeded = remainingPages / readingSpeed
            val daysNeeded = (hoursNeeded / 24.0).toLong()
            return LocalDate.now().plusDays(daysNeeded)
        }
    
    val hoursNeededToComplete: Double
        get() = if (readingSpeed > 0) remainingPages / readingSpeed else 0.0
    
    val isOnTrack: Boolean
        get() {
            if (dailyPages <= 0) return false
            val daysNeeded = (remainingPages.toDouble() / dailyPages).toLong()
            return daysNeeded <= daysUntilTarget
        }
    
    val requiredDailyPages: Int
        get() = if (daysUntilTarget > 0) (remainingPages / daysUntilTarget.toDouble()).toInt() else remainingPages
    
    val backlogDays: Int
        get() {
            if (dailyPages <= 0) return 0
            val daysNeeded = (remainingPages.toDouble() / dailyPages).toLong()
            return (daysNeeded - daysUntilTarget).toInt().coerceAtLeast(0)
        }
    
    fun calculateOptimalDailyPages(targetHoursPerDay: Double): Int {
        return if (readingSpeed > 0) {
            (targetHoursPerDay * readingSpeed).toInt()
        } else {
            requiredDailyPages
        }
    }
    
    fun calculateCompletionDateWithHours(hoursPerDay: Double): LocalDate? {
        if (readingSpeed <= 0) return null
        val totalHoursNeeded = remainingPages / readingSpeed
        val daysNeeded = (totalHoursNeeded / hoursPerDay).toLong()
        return LocalDate.now().plusDays(daysNeeded)
    }
    
    fun calculateBacklogRecoveryTime(extraHoursPerDay: Double): Int {
        if (readingSpeed <= 0 || extraHoursPerDay <= 0) return 0
        val extraPagesPerDay = extraHoursPerDay * readingSpeed
        val backlogPages = (backlogDays * dailyPages).coerceAtLeast(0)
        return if (extraPagesPerDay > 0) (backlogPages / extraPagesPerDay).toInt() else 0
    }
}