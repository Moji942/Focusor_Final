package com.focusor.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.util.Date

@Entity(tableName = "ai_conversations")
data class AIConversation(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val provider: AIProvider,
    val model: String,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date(),
    val totalTokensUsed: Int = 0,
    val estimatedCost: BigDecimal = BigDecimal.ZERO,
    val isBookmarked: Boolean = false,
    val tags: List<String> = emptyList()
) {
    // Get conversation summary (first message or title)
    val summary: String
        get() = if (title.isNotBlank()) title else "New Conversation"
    
    // Check if conversation was recently updated (within 24 hours)
    val isRecentlyUpdated: Boolean
        get() {
            val hoursSinceUpdate = (Date().time - updatedAt.time) / (60 * 60 * 1000)
            return hoursSinceUpdate <= 24
        }
}

@Entity(
    tableName = "ai_messages",
    foreignKeys = [
        androidx.room.ForeignKey(
            entity = AIConversation::class,
            parentColumns = ["id"],
            childColumns = ["conversationId"],
            onDelete = androidx.room.ForeignKey.CASCADE
        )
    ],
    indices = [androidx.room.Index("conversationId")]
)
data class AIMessage(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val conversationId: Long,
    val content: String,
    val role: MessageRole,
    val tokensUsed: Int = 0,
    val estimatedCost: BigDecimal = BigDecimal.ZERO,
    val timestamp: Date = Date(),
    val isEdited: Boolean = false,
    val editedAt: Date? = null
) {
    // Get word count
    val wordCount: Int
        get() = content.trim().split("\\s+".toRegex()).size
    
    // Get character count
    val characterCount: Int
        get() = content.length
    
    // Get reading time estimate (assuming 200 words per minute)
    val estimatedReadingTimeMinutes: Int
        get() = maxOf(1, wordCount / 200)
    
    // Get content preview (first 100 characters)
    val contentPreview: String
        get() = if (content.length > 100) {
            content.take(100) + "..."
        } else {
            content
        }
}

enum class AIProvider {
    OPENAI,
    GEMINI,
    CLAUDE,
    CUSTOM
}

enum class MessageRole {
    USER,
    ASSISTANT,
    SYSTEM
}