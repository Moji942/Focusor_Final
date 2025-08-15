package com.focusor.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.focusor.app.data.local.entity.TransactionEntity
import com.focusor.app.data.local.entity.TransactionType
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface TransactionDao {
    
    @Query("SELECT * FROM transactions ORDER BY transactionDate DESC")
    fun getAllTransactions(): Flow<List<TransactionEntity>>
    
    @Query("SELECT * FROM transactions WHERE cardId = :cardId ORDER BY transactionDate DESC")
    fun getTransactionsByCard(cardId: Long): Flow<List<TransactionEntity>>
    
    @Query("SELECT * FROM transactions WHERE transactionDate BETWEEN :startDate AND :endDate ORDER BY transactionDate DESC")
    fun getTransactionsInDateRange(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<TransactionEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity): Long
    
    @Update
    suspend fun updateTransaction(transaction: TransactionEntity)
    
    @Delete
    suspend fun deleteTransaction(transaction: TransactionEntity)
    
    @Query("SELECT SUM(amount) FROM transactions WHERE transactionType = :type AND transactionDate BETWEEN :startDate AND :endDate")
    suspend fun getTotalByType(type: TransactionType, startDate: LocalDateTime, endDate: LocalDateTime): java.math.BigDecimal?
    
    @Query("SELECT category, SUM(amount) as total FROM transactions WHERE transactionType = :type AND transactionDate BETWEEN :startDate AND :endDate GROUP BY category ORDER BY total DESC")
    suspend fun getCategoryTotals(type: TransactionType, startDate: LocalDateTime, endDate: LocalDateTime): List<CategoryTotal>
    
    @Query("SELECT * FROM transactions WHERE isRecurring = 1 ORDER BY transactionDate DESC")
    fun getRecurringTransactions(): Flow<List<TransactionEntity>>
    
    @Query("SELECT DISTINCT category FROM transactions ORDER BY category ASC")
    suspend fun getAllCategories(): List<String>
}

data class CategoryTotal(
    val category: String,
    val total: java.math.BigDecimal
)