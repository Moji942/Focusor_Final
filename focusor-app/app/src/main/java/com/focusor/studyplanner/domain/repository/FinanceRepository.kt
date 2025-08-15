package com.focusor.studyplanner.domain.repository

import com.focusor.studyplanner.data.entity.*
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
import java.time.LocalDateTime

interface FinanceRepository {
    // Card operations
    fun getAllActiveCards(): Flow<List<CardEntity>>
    suspend fun getCardById(id: Long): CardEntity?
    suspend fun insertCard(card: CardEntity): Long
    suspend fun updateCard(card: CardEntity)
    suspend fun deleteCard(card: CardEntity)
    suspend fun updateBalance(cardId: Long, balance: BigDecimal)
    
    // Transaction operations
    fun getAllTransactions(): Flow<List<TransactionEntity>>
    fun getTransactionsByCard(cardId: Long): Flow<List<TransactionEntity>>
    suspend fun insertTransaction(transaction: TransactionEntity): Long
    suspend fun updateTransaction(transaction: TransactionEntity)
    suspend fun deleteTransaction(transaction: TransactionEntity)
    
    // Installment operations
    fun getActiveInstallments(): Flow<List<InstallmentEntity>>
    fun getInstallmentsByCard(cardId: Long): Flow<List<InstallmentEntity>>
    suspend fun insertInstallment(installment: InstallmentEntity): Long
    suspend fun updateInstallment(installment: InstallmentEntity)
    suspend fun incrementInstallmentPayment(installmentId: Long)
    
    // Calculations
    suspend fun getTotalBalance(): BigDecimal
    suspend fun getMonthlyIncome(startDate: LocalDateTime, endDate: LocalDateTime): BigDecimal
    suspend fun getMonthlyExpenses(startDate: LocalDateTime, endDate: LocalDateTime): BigDecimal
}