package com.focusor.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(tableName = "subjects")
data class SubjectEntity(
    @PrimaryKey(autoGenerate = true)
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
)