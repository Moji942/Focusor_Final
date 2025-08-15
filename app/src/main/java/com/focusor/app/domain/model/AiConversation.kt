package com.focusor.app.domain.model

import java.time.LocalDateTime

data class AiConversation(
    val id: Long = 0,
    val title: String,
    val provider: String, // "openai", "gemini", "claude", "custom"
    val model: String,
    val totalTokens: Int = 0,
    val estimatedCost: Double = 0.0,
    val isActive: Boolean = true,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val messages: List<AiMessage> = emptyList()
) {
    
    val messageCount: Int
        get() = messages.size
    
    val userMessageCount: Int
        get() = messages.count { it.role == "user" }
    
    val assistantMessageCount: Int
        get() = messages.count { it.role == "assistant" }
    
    val averageTokensPerMessage: Double
        get() = if (messages.isNotEmpty()) totalTokens.toDouble() / messages.size else 0.0
    
    val lastMessageTime: LocalDateTime?
        get() = messages.maxByOrNull { it.timestamp }?.timestamp
    
    fun isValid(): Boolean {
        return title.isNotBlank() && provider.isNotBlank() && model.isNotBlank()
    }
    
    fun getProviderDisplayName(): String {
        return when (provider.lowercase()) {
            "openai" -> "OpenAI"
            "gemini" -> "Google Gemini"
            "claude" -> "Anthropic Claude"
            "custom" -> "Custom"
            else -> provider
        }
    }
    
    fun calculateCostPerMessage(): Double {
        return if (messageCount > 0) estimatedCost / messageCount else 0.0
    }
}

data class AiMessage(
    val id: Long = 0,
    val conversationId: Long,
    val role: String, // "user", "assistant", "system"
    val content: String,
    val tokens: Int = 0,
    val timestamp: LocalDateTime = LocalDateTime.now()
) {
    
    val isUserMessage: Boolean
        get() = role == "user"
    
    val isAssistantMessage: Boolean
        get() = role == "assistant"
    
    val isSystemMessage: Boolean
        get() = role == "system"
    
    val wordCount: Int
        get() = content.split("\\s+".toRegex()).filter { it.isNotBlank() }.size
    
    val characterCount: Int
        get() = content.length
    
    fun isValid(): Boolean {
        return content.isNotBlank() && role.isNotBlank()
    }
}