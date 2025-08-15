package com.focusor.studyplanner.data.repository

import com.focusor.studyplanner.data.dao.CardDao
import com.focusor.studyplanner.data.dao.InstallmentDao
import com.focusor.studyplanner.data.dao.TransactionDao
import com.focusor.studyplanner.data.entity.*
import com.focusor.studyplanner.domain.repository.FinanceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.math.BigDecimal
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FinanceRepositoryImpl @Inject constructor(
    private val cardDao: CardDao,
    private val transactionDao: TransactionDao,
    private val installmentDao: InstallmentDao
) : FinanceRepository {
    
    override fun getAllActiveCards(): Flow<List<CardEntity>> {
        return cardDao.getAllActiveCards()
    }
    
    override suspend fun getCardById(id: Long): CardEntity? {
        return cardDao.getCardById(id)
    }
    
    override suspend fun insertCard(card: CardEntity): Long {
        return cardDao.insertCard(card)
    }
    
    override suspend fun updateCard(card: CardEntity) {
        cardDao.updateCard(card)
    }
    
    override suspend fun deleteCard(card: CardEntity) {
        cardDao.deleteCard(card)
    }
    
    override suspend fun updateBalance(cardId: Long, balance: BigDecimal) {
        cardDao.updateBalance(cardId, balance)
    }
    
    override fun getAllTransactions(): Flow<List<TransactionEntity>> {
        return transactionDao.getAllTransactions()
    }
    
    override fun getTransactionsByCard(cardId: Long): Flow<List<TransactionEntity>> {
        return transactionDao.getTransactionsByCard(cardId)
    }
    
    override suspend fun insertTransaction(transaction: TransactionEntity): Long {
        val id = transactionDao.insertTransaction(transaction)
        
        // Update card balance based on transaction type
        when (transaction.type) {
            TransactionType.INCOME -> {
                cardDao.adjustBalance(transaction.cardId, transaction.amount)
            }
            TransactionType.EXPENSE -> {
                cardDao.adjustBalance(transaction.cardId, transaction.amount.negate())
            }
            TransactionType.TRANSFER -> {
                // Handle transfer logic if needed
            }
        }
        
        return id
    }
    
    override suspend fun updateTransaction(transaction: TransactionEntity) {
        transactionDao.updateTransaction(transaction)
    }
    
    override suspend fun deleteTransaction(transaction: TransactionEntity) {
        transactionDao.deleteTransaction(transaction)
        
        // Reverse the balance change
        when (transaction.type) {
            TransactionType.INCOME -> {
                cardDao.adjustBalance(transaction.cardId, transaction.amount.negate())
            }
            TransactionType.EXPENSE -> {
                cardDao.adjustBalance(transaction.cardId, transaction.amount)
            }
            TransactionType.TRANSFER -> {
                // Handle transfer reversal if needed
            }
        }
    }
    
    override fun getActiveInstallments(): Flow<List<InstallmentEntity>> {
        return installmentDao.getActiveInstallments()
    }
    
    override fun getInstallmentsByCard(cardId: Long): Flow<List<InstallmentEntity>> {
        return installmentDao.getActiveInstallmentsByCard(cardId)
    }
    
    override suspend fun insertInstallment(installment: InstallmentEntity): Long {
        return installmentDao.insertInstallment(installment)
    }
    
    override suspend fun updateInstallment(installment: InstallmentEntity) {
        installmentDao.updateInstallment(installment)
    }
    
    override suspend fun incrementInstallmentPayment(installmentId: Long) {
        installmentDao.incrementPaidCount(installmentId)
        
        val installment = installmentDao.getAllInstallments().first().find { it.id == installmentId }
        installment?.let {
            if (it.paidCount + 1 >= it.installmentCount) {
                installmentDao.updateStatus(installmentId, InstallmentStatus.COMPLETED)
            }
            
            // Deduct payment from card
            cardDao.adjustBalance(it.sourceCardId, it.monthlyPayment.negate())
        }
    }
    
    override suspend fun getTotalBalance(): BigDecimal {
        return cardDao.getTotalBalance() ?: BigDecimal.ZERO
    }
    
    override suspend fun getMonthlyIncome(startDate: LocalDateTime, endDate: LocalDateTime): BigDecimal {
        return transactionDao.getTotalByTypeInDateRange(TransactionType.INCOME, startDate, endDate) 
            ?: BigDecimal.ZERO
    }
    
    override suspend fun getMonthlyExpenses(startDate: LocalDateTime, endDate: LocalDateTime): BigDecimal {
        return transactionDao.getTotalByTypeInDateRange(TransactionType.EXPENSE, startDate, endDate)
            ?: BigDecimal.ZERO
    }
}