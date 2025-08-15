package com.focusor.app.domain.model

import java.time.LocalDateTime

data class StudySession(
    val id: Long = 0,
    val subjectId: Long,
    val startPage: Int,
    val endPage: Int,
    val durationMinutes: Int,
    val pagesRead: Int = endPage - startPage,
    val readingSpeed: Double = if (durationMinutes > 0) pagesRead.toDouble() / (durationMinutes / 60.0) else 0.0,
    val sessionDate: LocalDateTime = LocalDateTime.now(),
    val notes: String = ""
) {
    
    val durationHours: Double
        get() = durationMinutes / 60.0
    
    val efficiency: Double
        get() = if (durationMinutes > 0) pagesRead.toDouble() / durationMinutes else 0.0
    
    fun isValid(): Boolean {
        return startPage >= 0 && 
               endPage > startPage && 
               durationMinutes > 0 && 
               pagesRead > 0
    }
}