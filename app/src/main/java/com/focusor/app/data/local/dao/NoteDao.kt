package com.focusor.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.focusor.app.data.local.entity.NoteEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface NoteDao {
    
    @Query("SELECT * FROM notes ORDER BY isPinned DESC, updatedAt DESC")
    fun getAllNotes(): Flow<List<NoteEntity>>
    
    @Query("SELECT * FROM notes WHERE id = :noteId")
    suspend fun getNoteById(noteId: Long): NoteEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity): Long
    
    @Update
    suspend fun updateNote(note: NoteEntity)
    
    @Delete
    suspend fun deleteNote(note: NoteEntity)
    
    @Query("SELECT * FROM notes WHERE category = :category ORDER BY isPinned DESC, updatedAt DESC")
    fun getNotesByCategory(category: String): Flow<List<NoteEntity>>
    
    @Query("SELECT * FROM notes WHERE tags LIKE '%' || :tag || '%' ORDER BY isPinned DESC, updatedAt DESC")
    fun getNotesByTag(tag: String): Flow<List<NoteEntity>>
    
    @Query("SELECT * FROM notes WHERE title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%' ORDER BY isPinned DESC, updatedAt DESC")
    fun searchNotes(query: String): Flow<List<NoteEntity>>
    
    @Query("SELECT * FROM notes WHERE priority = :priority ORDER BY isPinned DESC, updatedAt DESC")
    fun getNotesByPriority(priority: Int): Flow<List<NoteEntity>>
    
    @Query("SELECT * FROM notes WHERE isCompleted = :completed ORDER BY isPinned DESC, updatedAt DESC")
    fun getNotesByCompletionStatus(completed: Boolean): Flow<List<NoteEntity>>
    
    @Query("SELECT * FROM notes WHERE reminderDate IS NOT NULL AND reminderDate <= :date ORDER BY reminderDate ASC")
    fun getNotesWithReminders(date: LocalDateTime): Flow<List<NoteEntity>>
    
    @Query("SELECT DISTINCT category FROM notes ORDER BY category ASC")
    suspend fun getAllCategories(): List<String>
    
    @Query("SELECT DISTINCT tags FROM notes WHERE tags != ''")
    suspend fun getAllTags(): List<String>
    
    @Query("UPDATE notes SET isPinned = :isPinned, updatedAt = :updatedAt WHERE id = :noteId")
    suspend fun updatePinStatus(noteId: Long, isPinned: Boolean, updatedAt: LocalDateTime)
    
    @Query("UPDATE notes SET isCompleted = :isCompleted, updatedAt = :updatedAt WHERE id = :noteId")
    suspend fun updateCompletionStatus(noteId: Long, isCompleted: Boolean, updatedAt: LocalDateTime)
}