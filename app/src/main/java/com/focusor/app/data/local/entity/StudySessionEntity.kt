package com.focusor.app.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    tableName = "study_sessions",
    foreignKeys = [
        ForeignKey(
            entity = SubjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["subjectId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class StudySessionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val subjectId: Long,
    val startPage: Int,
    val endPage: Int,
    val durationMinutes: Int,
    val pagesRead: Int = endPage - startPage,
    val readingSpeed: Double = if (durationMinutes > 0) pagesRead.toDouble() / (durationMinutes / 60.0) else 0.0,
    val sessionDate: LocalDateTime = LocalDateTime.now(),
    val notes: String = ""
)