package com.focusor.app.domain.model

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

data class Note(
    val id: Long = 0,
    val title: String,
    val content: String,
    val category: String = "General",
    val tags: String = "", // Comma-separated tags
    val priority: Int = 1, // 1-5 scale
    val isPinned: Boolean = false,
    val reminderDate: LocalDateTime? = null,
    val isCompleted: Boolean = false,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
) {
    
    val tagList: List<String>
        get() = if (tags.isBlank()) emptyList() else tags.split(",").map { it.trim() }
    
    val wordCount: Int
        get() = content.split("\\s+".toRegex()).filter { it.isNotBlank() }.size
    
    val characterCount: Int
        get() = content.length
    
    val hasReminder: Boolean
        get() = reminderDate != null
    
    val isReminderOverdue: Boolean
        get() = reminderDate?.isBefore(LocalDateTime.now()) == true
    
    val daysUntilReminder: Long?
        get() = reminderDate?.let { ChronoUnit.DAYS.between(LocalDateTime.now(), it) }
    
    val isReminderSoon: Boolean
        get() = daysUntilReminder?.let { it <= 1 } == true
    
    fun isValid(): Boolean {
        return title.isNotBlank() && content.isNotBlank()
    }
    
    fun matchesSearch(query: String): Boolean {
        val lowerQuery = query.lowercase()
        return title.lowercase().contains(lowerQuery) ||
               content.lowercase().contains(lowerQuery) ||
               category.lowercase().contains(lowerQuery) ||
               tagList.any { it.lowercase().contains(lowerQuery) }
    }
    
    fun hasTag(tag: String): Boolean {
        return tagList.any { it.equals(tag, ignoreCase = true) }
    }
    
    fun addTag(tag: String): Note {
        val newTags = if (tags.isBlank()) tag else "$tags, $tag"
        return copy(tags = newTags)
    }
    
    fun removeTag(tag: String): Note {
        val newTags = tagList.filter { !it.equals(tag, ignoreCase = true) }.joinToString(", ")
        return copy(tags = newTags)
    }
}