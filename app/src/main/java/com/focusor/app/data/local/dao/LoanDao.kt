package com.focusor.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.focusor.app.data.local.entity.LoanEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface LoanDao {
    
    @Query("SELECT * FROM loans WHERE isActive = 1 ORDER BY nextPaymentDate ASC")
    fun getAllActiveLoans(): Flow<List<LoanEntity>>
    
    @Query("SELECT * FROM loans WHERE id = :loanId")
    suspend fun getLoanById(loanId: Long): LoanEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLoan(loan: LoanEntity): Long
    
    @Update
    suspend fun updateLoan(loan: LoanEntity)
    
    @Delete
    suspend fun deleteLoan(loan: LoanEntity)
    
    @Query("UPDATE loans SET remainingAmount = :remainingAmount, nextPaymentDate = :nextPaymentDate, updatedAt = :updatedAt WHERE id = :loanId")
    suspend fun updateLoanPayment(loanId: Long, remainingAmount: java.math.BigDecimal, nextPaymentDate: LocalDate, updatedAt: java.time.LocalDateTime)
    
    @Query("SELECT * FROM loans WHERE nextPaymentDate <= :date AND isActive = 1 ORDER BY nextPaymentDate ASC")
    fun getLoansDueByDate(date: LocalDate): Flow<List<LoanEntity>>
    
    @Query("SELECT SUM(monthlyPayment) FROM loans WHERE isActive = 1")
    suspend fun getTotalMonthlyPayments(): java.math.BigDecimal?
    
    @Query("SELECT SUM(remainingAmount) FROM loans WHERE isActive = 1")
    suspend fun getTotalRemainingDebt(): java.math.BigDecimal?
}