package com.focusor.studyplanner.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ai_providers")
data class AIProviderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val endpoint: String,
    val apiKeyEncrypted: String, // Encrypted API key
    val model: String,
    val maxTokens: Int = 4096,
    val temperature: Double = 0.7,
    val rateLimit: Int = 60, // requests per minute
    val isActive: Boolean = true,
    val totalTokensUsed: Long = 0,
    val totalCost: Double = 0.0,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)