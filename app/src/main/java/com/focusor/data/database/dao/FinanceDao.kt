package com.focusor.data.database.dao

import androidx.room.*
import com.focusor.data.database.entities.FinanceCard
import com.focusor.data.database.entities.FinanceLoan
import com.focusor.data.database.entities.FinanceGoal
import com.focusor.data.database.entities.CardType
import com.focusor.data.database.entities.LoanType
import com.focusor.data.database.entities.GoalType
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
import java.util.Date

@Dao
interface FinanceDao {
    
    // FinanceCard operations
    @Query("SELECT * FROM finance_cards WHERE isActive = 1 ORDER BY name ASC")
    fun getAllActiveCards(): Flow<List<FinanceCard>>
    
    @Query("SELECT * FROM finance_cards ORDER BY createdAt DESC")
    fun getAllCards(): Flow<List<FinanceCard>>
    
    @Query("SELECT * FROM finance_cards WHERE id = :id")
    suspend fun getCardById(id: Long): FinanceCard?
    
    @Query("SELECT * FROM finance_cards WHERE cardType = :type AND isActive = 1")
    fun getCardsByType(type: CardType): Flow<List<FinanceCard>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCard(card: FinanceCard): Long
    
    @Update
    suspend fun updateCard(card: FinanceCard)
    
    @Delete
    suspend fun deleteCard(card: FinanceCard)
    
    @Query("UPDATE finance_cards SET currentBalance = :balance, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updateCardBalance(id: Long, balance: String, updatedAt: Date = Date()) // String for BigDecimal
    
    @Query("SELECT SUM(currentBalance) FROM finance_cards WHERE cardType = :type AND isActive = 1")
    suspend fun getTotalBalanceByType(type: CardType): String? // BigDecimal as String
    
    @Query("SELECT SUM(availableCredit) FROM finance_cards WHERE cardType = 'CREDIT' AND isActive = 1")
    suspend fun getTotalAvailableCredit(): String? // BigDecimal as String
    
    // FinanceLoan operations
    @Query("SELECT * FROM finance_loans WHERE isActive = 1 ORDER BY dueDate ASC")
    fun getAllActiveLoans(): Flow<List<FinanceLoan>>
    
    @Query("SELECT * FROM finance_loans ORDER BY createdAt DESC")
    fun getAllLoans(): Flow<List<FinanceLoan>>
    
    @Query("SELECT * FROM finance_loans WHERE id = :id")
    suspend fun getLoanById(id: Long): FinanceLoan?
    
    @Query("SELECT * FROM finance_loans WHERE loanType = :type AND isActive = 1")
    fun getLoansByType(type: LoanType): Flow<List<FinanceLoan>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLoan(loan: FinanceLoan): Long
    
    @Update
    suspend fun updateLoan(loan: FinanceLoan)
    
    @Delete
    suspend fun deleteLoan(loan: FinanceLoan)
    
    @Query("UPDATE finance_loans SET currentBalance = :balance, paidInstallments = :paidInstallments, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updateLoanProgress(id: Long, balance: String, paidInstallments: Int, updatedAt: Date = Date())
    
    @Query("SELECT SUM(currentBalance) FROM finance_loans WHERE isActive = 1")
    suspend fun getTotalLoanBalance(): String? // BigDecimal as String
    
    @Query("SELECT SUM(monthlyPayment) FROM finance_loans WHERE isActive = 1")
    suspend fun getTotalMonthlyLoanPayments(): String? // BigDecimal as String
    
    // FinanceGoal operations
    @Query("SELECT * FROM finance_goals WHERE isActive = 1 ORDER BY priority DESC, targetDate ASC")
    fun getAllActiveGoals(): Flow<List<FinanceGoal>>
    
    @Query("SELECT * FROM finance_goals ORDER BY createdAt DESC")
    fun getAllGoals(): Flow<List<FinanceGoal>>
    
    @Query("SELECT * FROM finance_goals WHERE id = :id")
    suspend fun getGoalById(id: Long): FinanceGoal?
    
    @Query("SELECT * FROM finance_goals WHERE goalType = :type AND isActive = 1")
    fun getGoalsByType(type: GoalType): Flow<List<FinanceGoal>>
    
    @Query("SELECT * FROM finance_goals WHERE isCompleted = 0 AND isActive = 1 ORDER BY priority DESC")
    fun getIncompleteGoals(): Flow<List<FinanceGoal>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: FinanceGoal): Long
    
    @Update
    suspend fun updateGoal(goal: FinanceGoal)
    
    @Delete
    suspend fun deleteGoal(goal: FinanceGoal)
    
    @Query("UPDATE finance_goals SET currentAmount = :amount, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updateGoalProgress(id: Long, amount: String, updatedAt: Date = Date())
    
    @Query("UPDATE finance_goals SET isCompleted = :isCompleted, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updateGoalCompletion(id: Long, isCompleted: Boolean, updatedAt: Date = Date())
    
    // Analytics and Dashboard queries
    @Query("SELECT COUNT(*) FROM finance_cards WHERE isActive = 1")
    suspend fun getActiveCardCount(): Int
    
    @Query("SELECT COUNT(*) FROM finance_loans WHERE isActive = 1")
    suspend fun getActiveLoanCount(): Int
    
    @Query("SELECT COUNT(*) FROM finance_goals WHERE isActive = 1 AND isCompleted = 0")
    suspend fun getActiveGoalCount(): Int
    
    @Query("""
        SELECT SUM(
            CASE WHEN cardType = 'CREDIT' THEN currentBalance ELSE 0 END
        ) FROM finance_cards WHERE isActive = 1
    """)
    suspend fun getTotalCreditCardDebt(): String? // BigDecimal as String
    
    @Query("""
        SELECT AVG(
            CASE 
                WHEN cardType = 'CREDIT' AND creditLimit > 0 
                THEN (currentBalance * 100.0) / creditLimit 
                ELSE 0 
            END
        ) FROM finance_cards WHERE isActive = 1 AND cardType = 'CREDIT'
    """)
    suspend fun getAverageCreditUtilization(): Double?
    
    @Query("""
        SELECT fc.name, fc.dueDate, fc.currentBalance, fc.minimumPayment
        FROM finance_cards fc
        WHERE fc.isActive = 1 
        AND fc.cardType = 'CREDIT'
        AND (
            (fc.dueDate - strftime('%d', 'now')) BETWEEN 0 AND 7
            OR (fc.dueDate + 30 - strftime('%d', 'now')) BETWEEN 0 AND 7
        )
        ORDER BY fc.dueDate ASC
    """)
    suspend fun getUpcomingCardPayments(): List<CardPaymentInfo>
    
    @Query("""
        SELECT fl.name, fl.dueDate, fl.monthlyPayment, fl.currentBalance
        FROM finance_loans fl
        WHERE fl.isActive = 1 
        AND (
            (fl.dueDate - strftime('%d', 'now')) BETWEEN 0 AND 7
            OR (fl.dueDate + 30 - strftime('%d', 'now')) BETWEEN 0 AND 7
        )
        ORDER BY fl.dueDate ASC
    """)
    suspend fun getUpcomingLoanPayments(): List<LoanPaymentInfo>
    
    @Query("""
        SELECT fg.name, fg.targetDate, fg.targetAmount, fg.currentAmount
        FROM finance_goals fg
        WHERE fg.isActive = 1 
        AND fg.isCompleted = 0
        AND fg.targetDate IS NOT NULL
        AND fg.targetDate BETWEEN date('now') AND date('now', '+30 days')
        ORDER BY fg.targetDate ASC
    """)
    suspend fun getUpcomingGoalDeadlines(): List<GoalDeadlineInfo>
    
    // Financial health score calculation data
    @Query("""
        SELECT 
            (SELECT COALESCE(SUM(currentBalance), 0) FROM finance_cards WHERE cardType = 'CREDIT' AND isActive = 1) as totalCreditDebt,
            (SELECT COALESCE(SUM(creditLimit), 0) FROM finance_cards WHERE cardType = 'CREDIT' AND isActive = 1) as totalCreditLimit,
            (SELECT COALESCE(SUM(currentBalance), 0) FROM finance_cards WHERE cardType = 'DEBIT' AND isActive = 1) as totalCash,
            (SELECT COALESCE(SUM(currentBalance), 0) FROM finance_loans WHERE isActive = 1) as totalLoanDebt,
            (SELECT COALESCE(SUM(monthlyPayment), 0) FROM finance_loans WHERE isActive = 1) as totalMonthlyLoanPayments,
            (SELECT COUNT(*) FROM finance_goals WHERE goalType = 'EMERGENCY_FUND' AND isActive = 1) as hasEmergencyFund
    """)
    suspend fun getFinancialHealthData(): FinancialHealthData
    
    // Search functionality
    @Query("""
        SELECT * FROM finance_cards 
        WHERE name LIKE '%' || :query || '%' 
        OR bankName LIKE '%' || :query || '%' 
        OR notes LIKE '%' || :query || '%'
        ORDER BY name ASC
    """)
    fun searchCards(query: String): Flow<List<FinanceCard>>
    
    @Query("""
        SELECT * FROM finance_loans 
        WHERE name LIKE '%' || :query || '%' 
        OR lenderName LIKE '%' || :query || '%' 
        OR notes LIKE '%' || :query || '%'
        ORDER BY name ASC
    """)
    fun searchLoans(query: String): Flow<List<FinanceLoan>>
    
    @Query("""
        SELECT * FROM finance_goals 
        WHERE name LIKE '%' || :query || '%' 
        OR description LIKE '%' || :query || '%' 
        OR notes LIKE '%' || :query || '%'
        ORDER BY priority DESC, name ASC
    """)
    fun searchGoals(query: String): Flow<List<FinanceGoal>>
}

data class CardPaymentInfo(
    val name: String,
    val dueDate: Int,
    val currentBalance: String, // BigDecimal as String
    val minimumPayment: String  // BigDecimal as String
)

data class LoanPaymentInfo(
    val name: String,
    val dueDate: Int,
    val monthlyPayment: String, // BigDecimal as String
    val currentBalance: String  // BigDecimal as String
)

data class GoalDeadlineInfo(
    val name: String,
    val targetDate: Date,
    val targetAmount: String,   // BigDecimal as String
    val currentAmount: String   // BigDecimal as String
)

data class FinancialHealthData(
    val totalCreditDebt: String,        // BigDecimal as String
    val totalCreditLimit: String,       // BigDecimal as String
    val totalCash: String,              // BigDecimal as String
    val totalLoanDebt: String,          // BigDecimal as String
    val totalMonthlyLoanPayments: String, // BigDecimal as String
    val hasEmergencyFund: Int           // 0 or 1
)