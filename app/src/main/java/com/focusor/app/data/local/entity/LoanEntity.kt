package com.focusor.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(tableName = "loans")
data class LoanEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val originalAmount: BigDecimal,
    val remainingAmount: BigDecimal,
    val interestRate: BigDecimal,
    val termMonths: Int,
    val monthlyPayment: BigDecimal,
    val startDate: LocalDate,
    val nextPaymentDate: LocalDate,
    val paymentDay: Int, // Day of month
    val isActive: Boolean = true,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)