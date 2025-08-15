package com.focusor.data.database.dao

import androidx.room.*
import com.focusor.data.database.entities.*
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface NotesDao {
    @Query("SELECT * FROM notes ORDER BY isPinned DESC, updatedAt DESC")
    fun getAllNotes(): Flow<List<Note>>
    
    @Query("SELECT * FROM notes WHERE id = :id")
    suspend fun getNoteById(id: Long): Note?
    
    @Query("SELECT * FROM notes WHERE category = :category ORDER BY updatedAt DESC")
    fun getNotesByCategory(category: String): Flow<List<Note>>
    
    @Query("SELECT * FROM notes WHERE priority = :priority ORDER BY updatedAt DESC")
    fun getNotesByPriority(priority: NotePriority): Flow<List<Note>>
    
    @Query("SELECT * FROM notes WHERE reminderDate IS NOT NULL ORDER BY reminderDate ASC")
    fun getNotesWithReminders(): Flow<List<Note>>
    
    @Query("SELECT * FROM notes WHERE isPinned = 1 ORDER BY updatedAt DESC")
    fun getPinnedNotes(): Flow<List<Note>>
    
    @Query("SELECT DISTINCT category FROM notes ORDER BY category ASC")
    suspend fun getAllCategories(): List<String>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note): Long
    
    @Update
    suspend fun updateNote(note: Note)
    
    @Delete
    suspend fun deleteNote(note: Note)
    
    @Query("UPDATE notes SET lastAccessedAt = :accessTime WHERE id = :id")
    suspend fun updateLastAccessed(id: Long, accessTime: Date = Date())
    
    @Query("""
        SELECT * FROM notes 
        WHERE title LIKE '%' || :query || '%' 
        OR content LIKE '%' || :query || '%'
        OR category LIKE '%' || :query || '%'
        ORDER BY 
            CASE WHEN title LIKE '%' || :query || '%' THEN 1 ELSE 2 END,
            isPinned DESC, updatedAt DESC
    """)
    fun searchNotes(query: String): Flow<List<Note>>
    
    @Query("SELECT COUNT(*) FROM notes")
    suspend fun getNotesCount(): Int
    
    @Query("SELECT COUNT(*) FROM notes WHERE reminderDate BETWEEN :start AND :end")
    suspend fun getRemindersCount(start: Date, end: Date): Int
}

@Dao 
interface AIDao {
    @Query("SELECT * FROM ai_conversations ORDER BY updatedAt DESC")
    fun getAllConversations(): Flow<List<AIConversation>>
    
    @Query("SELECT * FROM ai_conversations WHERE id = :id")
    suspend fun getConversationById(id: Long): AIConversation?
    
    @Query("SELECT * FROM ai_conversations WHERE provider = :provider ORDER BY updatedAt DESC")
    fun getConversationsByProvider(provider: AIProvider): Flow<List<AIConversation>>
    
    @Query("SELECT * FROM ai_conversations WHERE isBookmarked = 1 ORDER BY updatedAt DESC")
    fun getBookmarkedConversations(): Flow<List<AIConversation>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConversation(conversation: AIConversation): Long
    
    @Update
    suspend fun updateConversation(conversation: AIConversation)
    
    @Delete
    suspend fun deleteConversation(conversation: AIConversation)
    
    @Query("SELECT * FROM ai_messages WHERE conversationId = :conversationId ORDER BY timestamp ASC")
    fun getMessagesForConversation(conversationId: Long): Flow<List<AIMessage>>
    
    @Query("SELECT * FROM ai_messages WHERE id = :id")
    suspend fun getMessageById(id: Long): AIMessage?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: AIMessage): Long
    
    @Update
    suspend fun updateMessage(message: AIMessage)
    
    @Delete
    suspend fun deleteMessage(message: AIMessage)
    
    @Query("DELETE FROM ai_messages WHERE conversationId = :conversationId")
    suspend fun deleteAllMessagesForConversation(conversationId: Long)
    
    @Query("SELECT COUNT(*) FROM ai_conversations")
    suspend fun getConversationCount(): Int
    
    @Query("SELECT SUM(totalTokensUsed) FROM ai_conversations")
    suspend fun getTotalTokensUsed(): Int?
    
    @Query("SELECT SUM(estimatedCost) FROM ai_conversations")
    suspend fun getTotalEstimatedCost(): String? // BigDecimal as String
    
    @Query("""
        SELECT * FROM ai_conversations 
        WHERE title LIKE '%' || :query || '%'
        ORDER BY updatedAt DESC
    """)
    fun searchConversations(query: String): Flow<List<AIConversation>>
}