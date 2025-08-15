package com.focusor.app.domain.model

import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

data class Loan(
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
) {
    
    val totalPaid: BigDecimal
        get() = originalAmount.subtract(remainingAmount)
    
    val progressPercentage: BigDecimal
        get() = if (originalAmount > BigDecimal.ZERO) {
            totalPaid.divide(originalAmount, 4, RoundingMode.HALF_UP).multiply(BigDecimal("100"))
        } else BigDecimal.ZERO
    
    val daysUntilNextPayment: Long
        get() = ChronoUnit.DAYS.between(LocalDate.now(), nextPaymentDate)
    
    val isPaymentDueSoon: Boolean
        get() = daysUntilNextPayment <= 7
    
    val isPaymentOverdue: Boolean
        get() = daysUntilNextPayment < 0
    
    val remainingPayments: Int
        get() = if (monthlyPayment > BigDecimal.ZERO) {
            (remainingAmount.divide(monthlyPayment, 0, RoundingMode.CEILING)).toInt()
        } else 0
    
    val estimatedPayoffDate: LocalDate
        get() = nextPaymentDate.plusMonths(remainingPayments.toLong())
    
    fun calculateTotalInterestPaid(): BigDecimal {
        val totalPayments = monthlyPayment.multiply(BigDecimal(termMonths))
        return totalPayments.subtract(originalAmount).coerceAtLeast(BigDecimal.ZERO)
    }
    
    fun calculateRemainingInterest(): BigDecimal {
        if (interestRate <= BigDecimal.ZERO) return BigDecimal.ZERO
        
        val monthlyRate = interestRate.divide(BigDecimal("1200"), 6, RoundingMode.HALF_UP)
        val remainingPayments = remainingPayments.toBigDecimal()
        
        // Simplified calculation - in reality, this would be more complex
        return remainingAmount.multiply(monthlyRate).multiply(remainingPayments)
            .setScale(2, RoundingMode.HALF_UP)
    }
    
    fun calculateTotalCostToPayoff(): BigDecimal {
        return remainingAmount.add(calculateRemainingInterest())
    }
    
    fun calculateEarlyPayoffSavings(extraPayment: BigDecimal): BigDecimal {
        if (extraPayment <= BigDecimal.ZERO) return BigDecimal.ZERO
        
        val currentTotalCost = calculateTotalCostToPayoff()
        val newMonthlyPayment = monthlyPayment.add(extraPayment)
        val newRemainingPayments = (remainingAmount.divide(newMonthlyPayment, 0, RoundingMode.CEILING)).toInt()
        val newRemainingInterest = remainingAmount
            .multiply(interestRate.divide(BigDecimal("1200"), 6, RoundingMode.HALF_UP))
            .multiply(newRemainingPayments.toBigDecimal())
        val newTotalCost = remainingAmount.add(newRemainingInterest)
        
        return currentTotalCost.subtract(newTotalCost).setScale(2, RoundingMode.HALF_UP)
    }
    
    fun getLoanHealthStatus(): LoanHealthStatus {
        return when {
            isPaymentOverdue -> LoanHealthStatus.OVERDUE
            isPaymentDueSoon -> LoanHealthStatus.DUE_SOON
            progressPercentage > BigDecimal("75") -> LoanHealthStatus.NEAR_COMPLETION
            progressPercentage > BigDecimal("50") -> LoanHealthStatus.GOOD_PROGRESS
            else -> LoanHealthStatus.EARLY_STAGE
        }
    }
}

enum class LoanHealthStatus {
    OVERDUE, DUE_SOON, NEAR_COMPLETION, GOOD_PROGRESS, EARLY_STAGE
}