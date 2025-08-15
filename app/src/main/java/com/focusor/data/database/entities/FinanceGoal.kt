package com.focusor.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.util.Date

@Entity(tableName = "finance_goals")
data class FinanceGoal(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String = "",
    val targetAmount: BigDecimal,
    val currentAmount: BigDecimal = BigDecimal.ZERO,
    val monthlyContribution: BigDecimal = BigDecimal.ZERO,
    val targetDate: Date?,
    val goalType: GoalType,
    val priority: Int = 3, // 1-5 scale
    val isCompleted: Boolean = false,
    val isActive: Boolean = true,
    val color: String = "#4CAF50", // Default green color
    val createdAt: Date = Date(),
    val updatedAt: Date = Date(),
    val notes: String = ""
) {
    // Calculate remaining amount
    val remainingAmount: BigDecimal
        get() = maxOf(BigDecimal.ZERO, targetAmount - currentAmount)
    
    // Calculate progress percentage
    val progressPercentage: Float
        get() = if (targetAmount > BigDecimal.ZERO) {
            (currentAmount.divide(targetAmount, 4, BigDecimal.ROUND_HALF_UP) * BigDecimal(100)).toFloat()
        } else {
            0f
        }
    
    // Calculate months needed to reach goal with current contribution
    fun getMonthsToGoal(): Int {
        return if (monthlyContribution > BigDecimal.ZERO && remainingAmount > BigDecimal.ZERO) {
            remainingAmount.divide(monthlyContribution, 0, BigDecimal.ROUND_UP).toInt()
        } else {
            Int.MAX_VALUE
        }
    }
    
    // Calculate estimated completion date based on current contribution
    fun getEstimatedCompletionDate(): Date? {
        return if (monthlyContribution > BigDecimal.ZERO) {
            val monthsNeeded = getMonthsToGoal()
            if (monthsNeeded < Int.MAX_VALUE) {
                val calendar = java.util.Calendar.getInstance()
                calendar.add(java.util.Calendar.MONTH, monthsNeeded)
                calendar.time
            } else {
                null
            }
        } else {
            null
        }
    }
    
    // Calculate required monthly contribution to meet target date
    fun getRequiredMonthlyContribution(): BigDecimal? {
        return targetDate?.let { target ->
            val now = Date()
            if (target.after(now)) {
                val monthsRemaining = getMonthsBetweenDates(now, target)
                if (monthsRemaining > 0) {
                    remainingAmount.divide(BigDecimal(monthsRemaining), 2, BigDecimal.ROUND_HALF_UP)
                } else {
                    null
                }
            } else {
                null
            }
        }
    }
    
    // Check if goal is on track
    val isOnTrack: Boolean
        get() {
            val estimatedDate = getEstimatedCompletionDate()
            return targetDate?.let { target ->
                estimatedDate?.let { estimated ->
                    !estimated.after(target)
                } ?: false
            } ?: true
        }
    
    // Check if goal is overdue
    val isOverdue: Boolean
        get() = targetDate?.let { target ->
            !isCompleted && target.before(Date())
        } ?: false
    
    // Calculate days remaining to target date
    fun getDaysRemaining(): Long? {
        return targetDate?.let { target ->
            val now = Date()
            if (target.after(now)) {
                (target.time - now.time) / (24 * 60 * 60 * 1000)
            } else {
                0
            }
        }
    }
    
    private fun getMonthsBetweenDates(start: Date, end: Date): Int {
        val calendar1 = java.util.Calendar.getInstance()
        val calendar2 = java.util.Calendar.getInstance()
        calendar1.time = start
        calendar2.time = end
        
        var months = 0
        while (calendar1.before(calendar2)) {
            calendar1.add(java.util.Calendar.MONTH, 1)
            months++
        }
        return months
    }
}

enum class GoalType {
    EMERGENCY_FUND,
    VACATION,
    HOME_PURCHASE,
    CAR_PURCHASE,
    RETIREMENT,
    EDUCATION,
    DEBT_PAYOFF,
    INVESTMENT,
    OTHER
}