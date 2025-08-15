package com.focusor.studyplanner.data.dao

import androidx.room.*
import com.focusor.studyplanner.data.entity.InstallmentEntity
import com.focusor.studyplanner.data.entity.InstallmentStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.math.BigDecimal
import java.time.LocalDate

@Dao
interface InstallmentDao {
    
    @Insert
    suspend fun insertInstallment(installment: InstallmentEntity): Long
    
    @Update
    suspend fun updateInstallment(installment: InstallmentEntity)
    
    @Delete
    suspend fun deleteInstallment(installment: InstallmentEntity)
    
    @Query("SELECT * FROM installments WHERE status = 'ACTIVE' ORDER BY startDate ASC")
    fun getActiveInstallments(): Flow<List<InstallmentEntity>>
    
    @Query("SELECT * FROM installments ORDER BY createdAt DESC")
    fun getAllInstallments(): Flow<List<InstallmentEntity>>
    
    @Query("SELECT * FROM installments WHERE sourceCardId = :cardId AND status = 'ACTIVE'")
    fun getActiveInstallmentsByCard(cardId: Long): Flow<List<InstallmentEntity>>
    
    @Query("SELECT SUM(monthlyPayment) FROM installments WHERE sourceCardId = :cardId AND status = 'ACTIVE'")
    suspend fun getTotalMonthlyPaymentForCard(cardId: Long): BigDecimal?
    
    @Query("UPDATE installments SET paidCount = paidCount + 1, updatedAt = :timestamp WHERE id = :installmentId")
    suspend fun incrementPaidCount(installmentId: Long, timestamp: Long = System.currentTimeMillis())
    
    @Query("UPDATE installments SET status = :status, updatedAt = :timestamp WHERE id = :installmentId")
    suspend fun updateStatus(installmentId: Long, status: InstallmentStatus, timestamp: Long = System.currentTimeMillis())
    
    @Query("SELECT * FROM installments WHERE status = 'ACTIVE' AND (paidCount * 30 + startDate) <= :date")
    fun getDueInstallments(date: Long): Flow<List<InstallmentEntity>>
    
    @Query("SELECT SUM((installmentCount - paidCount) * monthlyPayment) FROM installments WHERE status = 'ACTIVE'")
    suspend fun getTotalRemainingAmount(): BigDecimal?
}