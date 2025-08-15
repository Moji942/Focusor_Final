package com.focusor.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.util.Date

@Entity(tableName = "education_subjects")
data class EducationSubject(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val source: String,
    val totalPages: Int,
    val targetDate: Date,
    val currentPage: Int = 0,
    val readingSpeed: BigDecimal = BigDecimal.ZERO, // pages per hour
    val priority: Int = 1, // 1-5 scale
    val isCompleted: Boolean = false,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date(),
    val notes: String = "",
    val color: String = "#2E7D32" // Default green color
) {
    // Calculate remaining pages
    val remainingPages: Int
        get() = maxOf(0, totalPages - currentPage)
    
    // Calculate progress percentage
    val progressPercentage: Float
        get() = if (totalPages > 0) (currentPage.toFloat() / totalPages * 100) else 0f
    
    // Calculate remaining days
    fun getRemainingDays(): Long {
        val now = Date()
        return if (targetDate.after(now)) {
            (targetDate.time - now.time) / (24 * 60 * 60 * 1000)
        } else {
            0
        }
    }
    
    // Calculate daily pages required
    fun getDailyPagesRequired(): BigDecimal {
        val remainingDays = getRemainingDays()
        return if (remainingDays > 0) {
            BigDecimal(remainingPages).divide(BigDecimal(remainingDays), 2, BigDecimal.ROUND_HALF_UP)
        } else {
            BigDecimal.ZERO
        }
    }
    
    // Calculate estimated completion date based on current reading speed
    fun getEstimatedCompletionDate(): Date {
        return if (readingSpeed > BigDecimal.ZERO) {
            val hoursNeeded = BigDecimal(remainingPages).divide(readingSpeed, 2, BigDecimal.ROUND_HALF_UP)
            val daysNeeded = hoursNeeded.divide(BigDecimal(8), 0, BigDecimal.ROUND_UP) // Assuming 8 hours study per day
            val millisecondsToAdd = daysNeeded.toLong() * 24 * 60 * 60 * 1000
            Date(Date().time + millisecondsToAdd)
        } else {
            targetDate
        }
    }
    
    // Calculate total hours needed
    fun getHoursNeeded(): BigDecimal {
        return if (readingSpeed > BigDecimal.ZERO) {
            BigDecimal(remainingPages).divide(readingSpeed, 2, BigDecimal.ROUND_HALF_UP)
        } else {
            BigDecimal.ZERO
        }
    }
    
    // Check if subject is overdue
    val isOverdue: Boolean
        get() = !isCompleted && targetDate.before(Date())
}