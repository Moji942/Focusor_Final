package com.focusor.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val content: String,
    val category: String = "General",
    val priority: NotePriority = NotePriority.MEDIUM,
    val tags: List<String> = emptyList(),
    val reminderDate: Date? = null,
    val isCompleted: Boolean = false,
    val isPinned: Boolean = false,
    val color: String = "#FF9800", // Default orange color
    val createdAt: Date = Date(),
    val updatedAt: Date = Date(),
    val lastAccessedAt: Date = Date()
) {
    // Check if note has reminder
    val hasReminder: Boolean
        get() = reminderDate != null
    
    // Check if reminder is overdue
    val isReminderOverdue: Boolean
        get() = reminderDate?.let { it.before(Date()) } ?: false
    
    // Check if reminder is due soon (within 24 hours)
    val isReminderDueSoon: Boolean
        get() = reminderDate?.let { reminder ->
            val now = Date()
            val hoursUntilReminder = (reminder.time - now.time) / (60 * 60 * 1000)
            hoursUntilReminder in 0..24
        } ?: false
    
    // Get word count
    val wordCount: Int
        get() = content.trim().split("\\s+".toRegex()).size
    
    // Get character count
    val characterCount: Int
        get() = content.length
    
    // Get reading time estimate (assuming 200 words per minute)
    val estimatedReadingTimeMinutes: Int
        get() = maxOf(1, wordCount / 200)
    
    // Check if note was recently updated (within 24 hours)
    val isRecentlyUpdated: Boolean
        get() {
            val hoursSinceUpdate = (Date().time - updatedAt.time) / (60 * 60 * 1000)
            return hoursSinceUpdate <= 24
        }
    
    // Get content preview (first 100 characters)
    val contentPreview: String
        get() = if (content.length > 100) {
            content.take(100) + "..."
        } else {
            content
        }
}

enum class NotePriority {
    LOW,
    MEDIUM,
    HIGH,
    URGENT
}