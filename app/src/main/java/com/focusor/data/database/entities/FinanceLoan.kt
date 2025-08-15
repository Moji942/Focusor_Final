package com.focusor.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.util.Date

@Entity(tableName = "finance_loans")
data class FinanceLoan(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val lenderName: String,
    val originalAmount: BigDecimal,
    val currentBalance: BigDecimal,
    val interestRate: BigDecimal, // Annual percentage rate
    val monthlyPayment: BigDecimal,
    val totalInstallments: Int,
    val paidInstallments: Int = 0,
    val startDate: Date,
    val dueDate: Int = 1, // Day of month for payment
    val loanType: LoanType,
    val isActive: Boolean = true,
    val color: String = "#FF5722", // Default red color
    val createdAt: Date = Date(),
    val updatedAt: Date = Date(),
    val notes: String = ""
) {
    // Calculate remaining installments
    val remainingInstallments: Int
        get() = maxOf(0, totalInstallments - paidInstallments)
    
    // Calculate total interest paid
    val totalInterestPaid: BigDecimal
        get() = (monthlyPayment * BigDecimal(paidInstallments)) - (originalAmount - currentBalance)
    
    // Calculate total amount that will be paid
    val totalAmountToBePaid: BigDecimal
        get() = monthlyPayment * BigDecimal(totalInstallments)
    
    // Calculate total interest for the loan
    val totalInterest: BigDecimal
        get() = totalAmountToBePaid - originalAmount
    
    // Calculate progress percentage
    val progressPercentage: Float
        get() = if (totalInstallments > 0) {
            (paidInstallments.toFloat() / totalInstallments * 100)
        } else {
            0f
        }
    
    // Calculate monthly interest amount
    val monthlyInterest: BigDecimal
        get() = if (currentBalance > BigDecimal.ZERO) {
            val monthlyRate = interestRate.divide(BigDecimal(12 * 100), 6, BigDecimal.ROUND_HALF_UP)
            currentBalance * monthlyRate
        } else {
            BigDecimal.ZERO
        }
    
    // Calculate principal payment amount
    val monthlyPrincipal: BigDecimal
        get() = monthlyPayment - monthlyInterest
    
    // Get next payment date
    fun getNextPaymentDate(): Date {
        val calendar = java.util.Calendar.getInstance()
        calendar.set(java.util.Calendar.DAY_OF_MONTH, dueDate)
        
        // If the due date has passed this month, move to next month
        if (calendar.time.before(Date())) {
            calendar.add(java.util.Calendar.MONTH, 1)
        }
        
        return calendar.time
    }
    
    // Calculate expected payoff date
    fun getExpectedPayoffDate(): Date {
        val calendar = java.util.Calendar.getInstance()
        calendar.time = startDate
        calendar.add(java.util.Calendar.MONTH, totalInstallments)
        return calendar.time
    }
    
    // Check if payment is due soon (within 7 days)
    val isPaymentDueSoon: Boolean
        get() {
            val daysUntilDue = (getNextPaymentDate().time - Date().time) / (24 * 60 * 60 * 1000)
            return daysUntilDue <= 7 && daysUntilDue >= 0
        }
    
    // Calculate early payoff amount (if applicable)
    fun getEarlyPayoffAmount(): BigDecimal {
        return currentBalance // Simplified calculation
    }
    
    // Check if loan is overdue
    val isOverdue: Boolean
        get() = getNextPaymentDate().before(Date()) && isActive
}

enum class LoanType {
    PERSONAL,
    HOME,
    AUTO,
    EDUCATION,
    BUSINESS,
    OTHER
}