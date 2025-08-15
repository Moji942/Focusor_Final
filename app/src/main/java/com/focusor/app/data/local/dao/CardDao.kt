package com.focusor.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.focusor.app.data.local.entity.CardEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CardDao {
    
    @Query("SELECT * FROM cards WHERE isActive = 1 ORDER BY name ASC")
    fun getAllActiveCards(): Flow<List<CardEntity>>
    
    @Query("SELECT * FROM cards WHERE id = :cardId")
    suspend fun getCardById(cardId: Long): CardEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCard(card: CardEntity): Long
    
    @Update
    suspend fun updateCard(card: CardEntity)
    
    @Delete
    suspend fun deleteCard(card: CardEntity)
    
    @Query("UPDATE cards SET currentBalance = :balance, updatedAt = :updatedAt WHERE id = :cardId")
    suspend fun updateBalance(cardId: Long, balance: java.math.BigDecimal, updatedAt: java.time.LocalDateTime)
    
    @Query("SELECT SUM(currentBalance) FROM cards WHERE isActive = 1")
    suspend fun getTotalBalance(): java.math.BigDecimal?
    
    @Query("SELECT SUM(creditLimit) FROM cards WHERE isActive = 1")
    suspend fun getTotalCreditLimit(): java.math.BigDecimal?
    
    @Query("SELECT * FROM cards WHERE dueDate = :dayOfMonth AND isActive = 1")
    fun getCardsDueOnDay(dayOfMonth: Int): Flow<List<CardEntity>>
}