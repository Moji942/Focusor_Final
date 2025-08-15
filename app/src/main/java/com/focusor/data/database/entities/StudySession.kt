package com.focusor.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.util.Date

@Entity(
    tableName = "study_sessions",
    foreignKeys = [
        ForeignKey(
            entity = EducationSubject::class,
            parentColumns = ["id"],
            childColumns = ["subjectId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("subjectId")]
)
data class StudySession(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val subjectId: Long,
    val startPage: Int,
    val endPage: Int,
    val startTime: Date,
    val endTime: Date,
    val sessionDate: Date,
    val notes: String = "",
    val rating: Int = 3, // 1-5 scale for session quality
    val createdAt: Date = Date()
) {
    // Calculate pages read in this session
    val pagesRead: Int
        get() = maxOf(0, endPage - startPage)
    
    // Calculate session duration in minutes
    val durationMinutes: Long
        get() = (endTime.time - startTime.time) / (60 * 1000)
    
    // Calculate session duration in hours
    val durationHours: BigDecimal
        get() = BigDecimal(durationMinutes).divide(BigDecimal(60), 2, BigDecimal.ROUND_HALF_UP)
    
    // Calculate reading speed for this session (pages per hour)
    val readingSpeed: BigDecimal
        get() = if (durationHours > BigDecimal.ZERO) {
            BigDecimal(pagesRead).divide(durationHours, 2, BigDecimal.ROUND_HALF_UP)
        } else {
            BigDecimal.ZERO
        }
    
    // Check if this is a valid session
    val isValid: Boolean
        get() = pagesRead > 0 && durationMinutes > 0 && endTime.after(startTime)
}