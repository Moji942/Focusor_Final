package com.focusor.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.util.Date

@Entity(tableName = "finance_cards")
data class FinanceCard(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val bankName: String,
    val cardType: CardType,
    val currentBalance: BigDecimal,
    val creditLimit: BigDecimal = BigDecimal.ZERO,
    val monthlyPayment: BigDecimal = BigDecimal.ZERO,
    val dueDate: Int = 1, // Day of month (1-31)
    val interestRate: BigDecimal = BigDecimal.ZERO, // Annual percentage rate
    val isActive: Boolean = true,
    val color: String = "#1565C0", // Default blue color
    val createdAt: Date = Date(),
    val updatedAt: Date = Date(),
    val notes: String = ""
) {
    // Calculate available credit
    val availableCredit: BigDecimal
        get() = if (cardType == CardType.CREDIT) {
            creditLimit - currentBalance
        } else {
            currentBalance
        }
    
    // Calculate credit utilization percentage
    val creditUtilization: Float
        get() = if (cardType == CardType.CREDIT && creditLimit > BigDecimal.ZERO) {
            (currentBalance.divide(creditLimit, 4, BigDecimal.ROUND_HALF_UP) * BigDecimal(100)).toFloat()
        } else {
            0f
        }
    
    // Calculate monthly interest
    val monthlyInterest: BigDecimal
        get() = if (cardType == CardType.CREDIT && currentBalance > BigDecimal.ZERO) {
            val monthlyRate = interestRate.divide(BigDecimal(12 * 100), 6, BigDecimal.ROUND_HALF_UP)
            currentBalance * monthlyRate
        } else {
            BigDecimal.ZERO
        }
    
    // Get next due date
    fun getNextDueDate(): Date {
        val calendar = java.util.Calendar.getInstance()
        calendar.set(java.util.Calendar.DAY_OF_MONTH, dueDate)
        
        // If the due date has passed this month, move to next month
        if (calendar.time.before(Date())) {
            calendar.add(java.util.Calendar.MONTH, 1)
        }
        
        return calendar.time
    }
    
    // Calculate projected balance after next payment
    fun getProjectedBalance(additionalPayment: BigDecimal = BigDecimal.ZERO): BigDecimal {
        return when (cardType) {
            CardType.CREDIT -> {
                val totalPayment = monthlyPayment + additionalPayment
                maxOf(BigDecimal.ZERO, currentBalance + monthlyInterest - totalPayment)
            }
            CardType.DEBIT -> currentBalance - additionalPayment
            CardType.PREPAID -> currentBalance - additionalPayment
        }
    }
    
    // Check if payment is due soon (within 7 days)
    val isPaymentDueSoon: Boolean
        get() {
            val daysUntilDue = (getNextDueDate().time - Date().time) / (24 * 60 * 60 * 1000)
            return daysUntilDue <= 7 && daysUntilDue >= 0
        }
    
    // Calculate minimum payment required
    val minimumPayment: BigDecimal
        get() = if (cardType == CardType.CREDIT && currentBalance > BigDecimal.ZERO) {
            // Typically 2-3% of balance or minimum $25
            val percentagePayment = currentBalance * BigDecimal("0.02")
            maxOf(percentagePayment, BigDecimal("25"))
        } else {
            BigDecimal.ZERO
        }
}

enum class CardType {
    CREDIT,
    DEBIT,
    PREPAID
}