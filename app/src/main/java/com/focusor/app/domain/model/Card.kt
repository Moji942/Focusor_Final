package com.focusor.app.domain.model

import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

data class Card(
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
) {
    
    val availableCredit: BigDecimal
        get() = creditLimit.subtract(currentBalance)
    
    val creditUtilization: BigDecimal
        get() = if (creditLimit > BigDecimal.ZERO) {
            currentBalance.divide(creditLimit, 4, RoundingMode.HALF_UP).multiply(BigDecimal("100"))
        } else BigDecimal.ZERO
    
    val isOverLimit: Boolean
        get() = currentBalance > creditLimit
    
    val nextDueDate: LocalDate
        get() {
            val today = LocalDate.now()
            val currentMonthDue = LocalDate.of(today.year, today.month, dueDate)
            return if (currentMonthDue.isBefore(today)) {
                currentMonthDue.plusMonths(1)
            } else {
                currentMonthDue
            }
        }
    
    val daysUntilDue: Long
        get() = ChronoUnit.DAYS.between(LocalDate.now(), nextDueDate)
    
    val isDueSoon: Boolean
        get() = daysUntilDue <= 7
    
    val isOverdue: Boolean
        get() = daysUntilDue < 0
    
    fun calculateInterestCharge(): BigDecimal {
        if (interestRate <= BigDecimal.ZERO || currentBalance <= BigDecimal.ZERO) {
            return BigDecimal.ZERO
        }
        
        val monthlyRate = interestRate.divide(BigDecimal("1200"), 6, RoundingMode.HALF_UP) // APR / 12 / 100
        return currentBalance.multiply(monthlyRate).setScale(2, RoundingMode.HALF_UP)
    }
    
    fun calculateProjectedBalance(months: Int): BigDecimal {
        if (interestRate <= BigDecimal.ZERO) return currentBalance
        
        val monthlyRate = interestRate.divide(BigDecimal("1200"), 6, RoundingMode.HALF_UP)
        val rateMultiplier = BigDecimal.ONE.add(monthlyRate).pow(months)
        
        return currentBalance.multiply(rateMultiplier).setScale(2, RoundingMode.HALF_UP)
    }
    
    fun calculateMinimumPaymentRequired(): BigDecimal {
        return if (minimumPayment > BigDecimal.ZERO) {
            minimumPayment
        } else {
            // Standard minimum payment calculation (usually 1-3% of balance)
            currentBalance.multiply(BigDecimal("0.02")).setScale(2, RoundingMode.HALF_UP)
        }
    }
    
    fun calculatePayoffTime(monthlyPayment: BigDecimal): Int {
        if (monthlyPayment <= BigDecimal.ZERO || currentBalance <= BigDecimal.ZERO) return 0
        
        var remainingBalance = currentBalance
        var months = 0
        
        while (remainingBalance > BigDecimal.ZERO && months < 600) { // Max 50 years
            val interest = calculateInterestCharge()
            remainingBalance = remainingBalance.add(interest).subtract(monthlyPayment)
            months++
        }
        
        return months
    }
    
    fun getCreditHealthStatus(): CreditHealthStatus {
        return when {
            isOverLimit -> CreditHealthStatus.OVER_LIMIT
            creditUtilization > BigDecimal("90") -> CreditHealthStatus.HIGH_UTILIZATION
            creditUtilization > BigDecimal("70") -> CreditHealthStatus.MEDIUM_UTILIZATION
            creditUtilization > BigDecimal("30") -> CreditHealthStatus.GOOD_UTILIZATION
            else -> CreditHealthStatus.EXCELLENT_UTILIZATION
        }
    }
}

enum class CreditHealthStatus {
    OVER_LIMIT, HIGH_UTILIZATION, MEDIUM_UTILIZATION, GOOD_UTILIZATION, EXCELLENT_UTILIZATION
}