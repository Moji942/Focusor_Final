package com.focusor.studyplanner.data.dao

import androidx.room.*
import com.focusor.studyplanner.data.entity.CardEntity
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal

@Dao
interface CardDao {
    
    @Query("SELECT * FROM cards WHERE isActive = 1 ORDER BY bankName ASC")
    fun getAllActiveCards(): Flow<List<CardEntity>>
    
    @Query("SELECT * FROM cards ORDER BY createdAt DESC")
    fun getAllCards(): Flow<List<CardEntity>>
    
    @Query("SELECT * FROM cards WHERE id = :cardId")
    suspend fun getCardById(cardId: Long): CardEntity?
    
    @Insert
    suspend fun insertCard(card: CardEntity): Long
    
    @Update
    suspend fun updateCard(card: CardEntity)
    
    @Delete
    suspend fun deleteCard(card: CardEntity)
    
    @Query("UPDATE cards SET currentBalance = :balance, lastUpdated = :timestamp WHERE id = :cardId")
    suspend fun updateBalance(cardId: Long, balance: BigDecimal, timestamp: Long = System.currentTimeMillis())
    
    @Query("SELECT SUM(currentBalance) FROM cards WHERE isActive = 1")
    suspend fun getTotalBalance(): BigDecimal?
    
    @Query("UPDATE cards SET currentBalance = currentBalance + :amount WHERE id = :cardId")
    suspend fun adjustBalance(cardId: Long, amount: BigDecimal)
    
    @Query("SELECT * FROM cards WHERE currentBalance < :threshold AND isActive = 1")
    fun getCardsWithLowBalance(threshold: BigDecimal): Flow<List<CardEntity>>
}