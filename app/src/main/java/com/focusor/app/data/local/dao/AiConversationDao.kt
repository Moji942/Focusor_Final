package com.focusor.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.focusor.app.data.local.entity.AiConversationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AiConversationDao {
    
    @Query("SELECT * FROM ai_conversations WHERE isActive = 1 ORDER BY updatedAt DESC")
    fun getAllActiveConversations(): Flow<List<AiConversationEntity>>
    
    @Query("SELECT * FROM ai_conversations WHERE id = :conversationId")
    suspend fun getConversationById(conversationId: Long): AiConversationEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConversation(conversation: AiConversationEntity): Long
    
    @Update
    suspend fun updateConversation(conversation: AiConversationEntity)
    
    @Delete
    suspend fun deleteConversation(conversation: AiConversationEntity)
    
    @Query("SELECT * FROM ai_conversations WHERE provider = :provider ORDER BY updatedAt DESC")
    fun getConversationsByProvider(provider: String): Flow<List<AiConversationEntity>>
    
    @Query("UPDATE ai_conversations SET totalTokens = :totalTokens, estimatedCost = :estimatedCost, updatedAt = :updatedAt WHERE id = :conversationId")
    suspend fun updateConversationStats(conversationId: Long, totalTokens: Int, estimatedCost: Double, updatedAt: java.time.LocalDateTime)
    
    @Query("SELECT SUM(estimatedCost) FROM ai_conversations WHERE provider = :provider")
    suspend fun getTotalCostByProvider(provider: String): Double?
    
    @Query("SELECT SUM(totalTokens) FROM ai_conversations WHERE provider = :provider")
    suspend fun getTotalTokensByProvider(provider: String): Int?
}