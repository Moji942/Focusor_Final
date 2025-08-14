package com.focusor.studyplanner.domain.usecase.finance

import com.focusor.studyplanner.data.entity.*
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import kotlin.math.pow

class FinanceCalculationEngine @Inject constructor() {
    
    /**
     * Calculate available balance (current balance - committed future payments)
     */
    fun calculateAvailableBalance(
        currentBalance: BigDecimal,
        activeInstallments: List<InstallmentEntity>
    ): BigDecimal {
        val committedAmount = activeInstallments.sumOf { installment ->
            val remainingPayments = installment.installmentCount - installment.paidCount
            installment.monthlyPayment.multiply(BigDecimal(remainingPayments))
        }
        return currentBalance.subtract(committedAmount)
    }
    
    /**
     * Calculate projected balance at a future date
     */
    fun calculateProjectedBalance(
        currentBalance: BigDecimal,
        futureDate: LocalDate,
        installments: List<InstallmentEntity>,
        recurringIncome: List<Pair<BigDecimal, Int>>, // Amount and day of month
        currentDate: LocalDate = LocalDate.now()
    ): BigDecimal {
        var balance = currentBalance
        val monthsBetween = ChronoUnit.MONTHS.between(currentDate, futureDate).toInt()
        
        // Add recurring income
        recurringIncome.forEach { (amount, dayOfMonth) ->
            var checkDate = currentDate.withDayOfMonth(dayOfMonth.coerceIn(1, currentDate.lengthOfMonth()))
            repeat(monthsBetween + 1) {
                if (checkDate <= futureDate && checkDate >= currentDate) {
                    balance = balance.add(amount)
                }
                checkDate = checkDate.plusMonths(1)
            }
        }
        
        // Subtract installment payments
        installments.filter { it.status == InstallmentStatus.ACTIVE }.forEach { installment ->
            val paymentsUntilDate = calculatePaymentsUntilDate(installment, futureDate, currentDate)
            val totalPayment = installment.monthlyPayment.multiply(BigDecimal(paymentsUntilDate))
            balance = balance.subtract(totalPayment)
        }
        
        return balance
    }
    
    /**
     * Calculate number of installment payments until a specific date
     */
    private fun calculatePaymentsUntilDate(
        installment: InstallmentEntity,
        untilDate: LocalDate,
        fromDate: LocalDate = LocalDate.now()
    ): Int {
        var paymentCount = 0
        var nextPaymentDate = installment.startDate.plusMonths(installment.paidCount.toLong())
        
        while (nextPaymentDate <= untilDate && nextPaymentDate >= fromDate && 
               installment.paidCount + paymentCount < installment.installmentCount) {
            paymentCount++
            nextPaymentDate = nextPaymentDate.plusMonths(1)
        }
        
        return paymentCount
    }
    
    /**
     * Calculate monthly installment payment with interest
     */
    fun calculateMonthlyInstallment(
        principal: BigDecimal,
        annualInterestRate: Double,
        months: Int
    ): BigDecimal {
        if (months <= 0) return BigDecimal.ZERO
        
        if (annualInterestRate == 0.0) {
            return principal.divide(BigDecimal(months), 2, RoundingMode.HALF_UP)
        }
        
        val monthlyRate = annualInterestRate / 12 / 100
        val factor = (monthlyRate * (1 + monthlyRate).pow(months)) / 
                    ((1 + monthlyRate).pow(months) - 1)
        
        return principal.multiply(BigDecimal(factor))
            .setScale(2, RoundingMode.HALF_UP)
    }
    
    /**
     * Calculate total interest amount
     */
    fun calculateTotalInterest(
        principal: BigDecimal,
        monthlyPayment: BigDecimal,
        months: Int
    ): BigDecimal {
        val totalPaid = monthlyPayment.multiply(BigDecimal(months))
        return totalPaid.subtract(principal)
    }
    
    /**
     * Calculate cash flow for a period
     */
    fun calculateCashFlow(
        income: List<TransactionEntity>,
        expenses: List<TransactionEntity>,
        installmentPayments: BigDecimal
    ): BigDecimal {
        val totalIncome = income
            .filter { it.type == TransactionType.INCOME }
            .sumOf { it.amount }
        
        val totalExpenses = expenses
            .filter { it.type == TransactionType.EXPENSE }
            .sumOf { it.amount }
        
        return totalIncome.subtract(totalExpenses).subtract(installmentPayments)
    }
    
    /**
     * Calculate emergency fund coverage in months
     */
    fun calculateEmergencyFundMonths(
        totalSavings: BigDecimal,
        monthlyExpenses: BigDecimal
    ): Double {
        if (monthlyExpenses <= BigDecimal.ZERO) return Double.MAX_VALUE
        return totalSavings.divide(monthlyExpenses, 2, RoundingMode.HALF_UP).toDouble()
    }
    
    /**
     * Calculate savings required per month to reach a goal
     */
    fun calculateMonthlySavingsRequired(
        targetAmount: BigDecimal,
        currentSaved: BigDecimal,
        targetDate: LocalDate,
        currentDate: LocalDate = LocalDate.now()
    ): BigDecimal {
        val remaining = targetAmount.subtract(currentSaved)
        val monthsRemaining = ChronoUnit.MONTHS.between(currentDate, targetDate)
        
        if (monthsRemaining <= 0) return remaining
        
        return remaining.divide(BigDecimal(monthsRemaining), 2, RoundingMode.HALF_UP)
    }
    
    /**
     * Check if a savings goal is feasible with current cash flow
     */
    fun isSavingsGoalFeasible(
        monthlySavingsRequired: BigDecimal,
        netCashFlow: BigDecimal
    ): Boolean {
        return netCashFlow >= monthlySavingsRequired
    }
    
    /**
     * Calculate days until a card goes negative
     */
    fun calculateDaysUntilNegative(
        currentBalance: BigDecimal,
        dailyExpenseRate: BigDecimal,
        upcomingPayments: List<Pair<BigDecimal, LocalDate>>,
        currentDate: LocalDate = LocalDate.now()
    ): Int? {
        if (dailyExpenseRate <= BigDecimal.ZERO) return null
        
        var balance = currentBalance
        var checkDate = currentDate
        var daysCount = 0
        
        while (balance > BigDecimal.ZERO && daysCount < 365) {
            // Subtract daily expenses
            balance = balance.subtract(dailyExpenseRate)
            
            // Check for scheduled payments on this date
            upcomingPayments
                .filter { it.second == checkDate }
                .forEach { balance = balance.subtract(it.first) }
            
            if (balance < BigDecimal.ZERO) {
                return daysCount
            }
            
            checkDate = checkDate.plusDays(1)
            daysCount++
        }
        
        return if (balance < BigDecimal.ZERO) daysCount else null
    }
    
    /**
     * Calculate financial health score (0-100)
     */
    fun calculateFinancialHealthScore(
        emergencyFundMonths: Double,
        debtToIncomeRatio: Double,
        savingsRate: Double,
        onTimePaymentRate: Double
    ): Int {
        var score = 0
        
        // Emergency fund score (25 points max)
        score += when {
            emergencyFundMonths >= 6 -> 25
            emergencyFundMonths >= 3 -> 20
            emergencyFundMonths >= 1 -> 10
            else -> 0
        }
        
        // Debt to income ratio score (25 points max)
        score += when {
            debtToIncomeRatio <= 0.2 -> 25
            debtToIncomeRatio <= 0.35 -> 20
            debtToIncomeRatio <= 0.5 -> 10
            else -> 0
        }
        
        // Savings rate score (25 points max)
        score += when {
            savingsRate >= 0.2 -> 25
            savingsRate >= 0.1 -> 20
            savingsRate >= 0.05 -> 10
            else -> 0
        }
        
        // Payment history score (25 points max)
        score += (onTimePaymentRate * 25).toInt()
        
        return score.coerceIn(0, 100)
    }
    
    /**
     * Generate financial insights and recommendations
     */
    fun generateFinancialInsights(
        cards: List<CardEntity>,
        installments: List<InstallmentEntity>,
        monthlyIncome: BigDecimal,
        monthlyExpenses: BigDecimal,
        savings: BigDecimal
    ): List<String> {
        val insights = mutableListOf<String>()
        
        // Check for cards going negative
        cards.forEach { card ->
            val cardInstallments = installments.filter { it.sourceCardId == card.id }
            val availableBalance = calculateAvailableBalance(card.currentBalance, cardInstallments)
            
            if (availableBalance < BigDecimal.ZERO) {
                val deficit = availableBalance.abs()
                insights.add("${card.bankName} card will go negative by ${formatCurrency(deficit)}")
            } else if (availableBalance < BigDecimal(1000000)) {
                insights.add("${card.bankName} card balance is low: ${formatCurrency(availableBalance)}")
            }
        }
        
        // Check if income covers commitments
        val totalMonthlyCommitments = installments
            .filter { it.status == InstallmentStatus.ACTIVE }
            .sumOf { it.monthlyPayment }
            .add(monthlyExpenses)
        
        if (totalMonthlyCommitments > monthlyIncome) {
            val shortfall = totalMonthlyCommitments.subtract(monthlyIncome)
            insights.add("Monthly shortfall of ${formatCurrency(shortfall)} - reduce expenses or increase income")
        }
        
        // Emergency fund check
        val emergencyMonths = calculateEmergencyFundMonths(savings, monthlyExpenses)
        when {
            emergencyMonths < 1 -> insights.add("Critical: Emergency fund covers less than 1 month")
            emergencyMonths < 3 -> insights.add("Build emergency fund to cover 3-6 months of expenses")
            emergencyMonths >= 6 -> insights.add("Excellent emergency fund coverage: ${String.format("%.1f", emergencyMonths)} months")
        }
        
        // Debt optimization
        val highInterestInstallments = installments
            .filter { it.interestRate > 15 && it.status == InstallmentStatus.ACTIVE }
            .sortedByDescending { it.interestRate }
        
        if (highInterestInstallments.isNotEmpty()) {
            val highest = highInterestInstallments.first()
            val potentialSaving = calculateTotalInterest(
                highest.totalAmount.subtract(
                    highest.monthlyPayment.multiply(BigDecimal(highest.paidCount))
                ),
                highest.monthlyPayment,
                highest.installmentCount - highest.paidCount
            )
            insights.add("Pay off ${highest.title} first to save ${formatCurrency(potentialSaving)} in interest")
        }
        
        return insights
    }
    
    /**
     * Format currency for display (IRR/Toman)
     */
    private fun formatCurrency(amount: BigDecimal, inToman: Boolean = true): String {
        return if (inToman) {
            val toman = amount.divide(BigDecimal(10), 0, RoundingMode.HALF_UP)
            "${String.format("%,d", toman.toLong())} تومان"
        } else {
            "${String.format("%,d", amount.toLong())} ریال"
        }
    }
    
    /**
     * Calculate optimal payment order for debts (avalanche method)
     */
    fun calculateOptimalPaymentOrder(
        installments: List<InstallmentEntity>
    ): List<InstallmentEntity> {
        return installments
            .filter { it.status == InstallmentStatus.ACTIVE }
            .sortedByDescending { it.interestRate }
    }
    
    /**
     * Project savings goal completion date
     */
    fun projectSavingsGoalDate(
        targetAmount: BigDecimal,
        currentSaved: BigDecimal,
        monthlyContribution: BigDecimal,
        currentDate: LocalDate = LocalDate.now()
    ): LocalDate? {
        if (monthlyContribution <= BigDecimal.ZERO) return null
        
        val remaining = targetAmount.subtract(currentSaved)
        if (remaining <= BigDecimal.ZERO) return currentDate
        
        val monthsNeeded = remaining.divide(monthlyContribution, 0, RoundingMode.UP).toInt()
        return currentDate.plusMonths(monthsNeeded.toLong())
    }
}