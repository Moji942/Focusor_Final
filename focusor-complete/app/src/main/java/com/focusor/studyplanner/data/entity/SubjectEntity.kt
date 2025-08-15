package com.focusor.studyplanner.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "subjects")
data class SubjectEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val resourceType: String,
    val totalPages: Int,
    val completedPages: Int = 0,
    val startDate: LocalDate,
    val targetDate: LocalDate,
    val priority: Int = 3, // 1-5 scale
    val isActive: Boolean = true,
    val currentReadingSpeed: Double = 0.0, // pages per hour
    val averageReadingSpeed: Double = 0.0, // pages per hour
    val color: String = "#1976D2",
    val notes: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)