package com.focusor.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.focusor.app.data.local.entity.AiMessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AiMessageDao {
    
    @Query("SELECT * FROM ai_messages WHERE conversationId = :conversationId ORDER BY timestamp ASC")
    fun getMessagesByConversation(conversationId: Long): Flow<List<AiMessageEntity>>
    
    @Query("SELECT * FROM ai_messages WHERE conversationId = :conversationId ORDER BY timestamp ASC")
    suspend fun getMessagesByConversationSync(conversationId: Long): List<AiMessageEntity>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: AiMessageEntity): Long
    
    @Update
    suspend fun updateMessage(message: AiMessageEntity)
    
    @Delete
    suspend fun deleteMessage(message: AiMessageEntity)
    
    @Query("SELECT * FROM ai_messages WHERE conversationId = :conversationId AND role = :role ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getRecentMessagesByRole(conversationId: Long, role: String, limit: Int): List<AiMessageEntity>
    
    @Query("SELECT SUM(tokens) FROM ai_messages WHERE conversationId = :conversationId")
    suspend fun getTotalTokensForConversation(conversationId: Long): Int?
    
    @Query("DELETE FROM ai_messages WHERE conversationId = :conversationId")
    suspend fun deleteMessagesByConversation(conversationId: Long)
}