package com.focusor.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity(tableName = "cards")
data class CardEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val cardNumber: String, // Last 4 digits only
    val currentBalance: BigDecimal,
    val creditLimit: BigDecimal,
    val dueDate: Int, // Day of month
    val minimumPayment: BigDecimal = BigDecimal.ZERO,
    val interestRate: BigDecimal = BigDecimal.ZERO,
    val isActive: Boolean = true,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)