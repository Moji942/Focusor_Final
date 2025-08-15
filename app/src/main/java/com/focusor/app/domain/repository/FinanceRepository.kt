package com.focusor.app.domain.repository

import com.focusor.app.domain.model.Card
import com.focusor.app.domain.model.Transaction
import com.focusor.app.domain.model.Loan
import com.focusor.app.domain.model.TransactionType
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

interface FinanceRepository {
    
    // Card operations
    fun getAllActiveCards(): Flow<List<Card>>
    suspend fun getCardById(cardId: Long): Card?
    suspend fun insertCard(card: Card): Long
    suspend fun updateCard(card: Card)
    suspend fun deleteCard(card: Card)
    suspend fun updateBalance(cardId: Long, balance: BigDecimal)
    suspend fun getTotalBalance(): BigDecimal
    suspend fun getTotalCreditLimit(): BigDecimal
    suspend fun getCardsDueOnDay(dayOfMonth: Int): List<Card>
    
    // Transaction operations
    fun getAllTransactions(): Flow<List<Transaction>>
    fun getTransactionsByCard(cardId: Long): Flow<List<Transaction>>
    fun getTransactionsInDateRange(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<Transaction>>
    suspend fun insertTransaction(transaction: Transaction): Long
    suspend fun updateTransaction(transaction: Transaction)
    suspend fun deleteTransaction(transaction: Transaction)
    suspend fun getTotalByType(type: TransactionType, startDate: LocalDateTime, endDate: LocalDateTime): BigDecimal
    suspend fun getCategoryTotals(type: TransactionType, startDate: LocalDateTime, endDate: LocalDateTime): List<CategoryTotal>
    fun getRecurringTransactions(): Flow<List<Transaction>>
    suspend fun getAllCategories(): List<String>
    
    // Loan operations
    fun getAllActiveLoans(): Flow<List<Loan>>
    suspend fun getLoanById(loanId: Long): Loan?
    suspend fun insertLoan(loan: Loan): Long
    suspend fun updateLoan(loan: Loan)
    suspend fun deleteLoan(loan: Loan)
    suspend fun updateLoanPayment(loanId: Long, remainingAmount: BigDecimal, nextPaymentDate: LocalDate)
    suspend fun getLoansDueByDate(date: LocalDate): List<Loan>
    suspend fun getTotalMonthlyPayments(): BigDecimal
    suspend fun getTotalRemainingDebt(): BigDecimal
    
    // Financial analysis
    suspend fun calculateMonthlyCashFlow(startDate: LocalDate, endDate: LocalDate): CashFlowAnalysis
    suspend fun calculateProjectedBalance(cardId: Long, months: Int): BigDecimal
    suspend fun getFinancialHealthScore(): FinancialHealthScore
    suspend fun getUpcomingPayments(days: Int): List<UpcomingPayment>
}

data class CategoryTotal(
    val category: String,
    val total: BigDecimal
)

data class CashFlowAnalysis(
    val totalIncome: BigDecimal,
    val totalExpenses: BigDecimal,
    val netCashFlow: BigDecimal,
    val recurringIncome: BigDecimal,
    val recurringExpenses: BigDecimal,
    val discretionaryExpenses: BigDecimal
)

data class FinancialHealthScore(
    val score: Int, // 0-100
    val factors: List<FinancialFactor>
)

data class FinancialFactor(
    val name: String,
    val impact: Int, // -100 to 100
    val description: String
)

data class UpcomingPayment(
    val id: Long,
    val name: String,
    val amount: BigDecimal,
    val dueDate: LocalDate,
    val type: PaymentType
)

enum class PaymentType {
    CARD_PAYMENT, LOAN_PAYMENT, RECURRING_EXPENSE
}