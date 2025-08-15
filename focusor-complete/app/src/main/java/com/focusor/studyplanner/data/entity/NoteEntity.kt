package com.focusor.studyplanner.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val content: String, // HTML or Markdown content
    val categoryId: Long? = null,
    val tags: String? = null, // JSON array of tags
    val priority: Int = 3, // 1-5 scale
    val isPinned: Boolean = false,
    val color: String = "#FFFFFF",
    val reminderDateTime: LocalDateTime? = null,
    val reminderRepeatPattern: String? = null, // DAILY, WEEKLY, MONTHLY, YEARLY
    val attachments: String? = null, // JSON array of file paths
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)