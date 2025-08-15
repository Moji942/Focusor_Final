package com.focusor.studyplanner.data.dao

import androidx.room.*
import com.focusor.studyplanner.data.entity.SubjectEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SubjectDao {
    
    @Query("SELECT * FROM subjects WHERE isActive = 1 ORDER BY priority DESC, targetDate ASC")
    fun getAllActiveSubjects(): Flow<List<SubjectEntity>>
    
    @Query("SELECT * FROM subjects ORDER BY createdAt DESC")
    fun getAllSubjects(): Flow<List<SubjectEntity>>
    
    @Query("SELECT * FROM subjects WHERE id = :subjectId")
    suspend fun getSubjectById(subjectId: Long): SubjectEntity?
    
    @Insert
    suspend fun insertSubject(subject: SubjectEntity): Long
    
    @Update
    suspend fun updateSubject(subject: SubjectEntity)
    
    @Delete
    suspend fun deleteSubject(subject: SubjectEntity)
    
    @Query("UPDATE subjects SET completedPages = :completedPages, updatedAt = :timestamp WHERE id = :subjectId")
    suspend fun updateProgress(subjectId: Long, completedPages: Int, timestamp: Long = System.currentTimeMillis())
    
    @Query("UPDATE subjects SET currentReadingSpeed = :speed, averageReadingSpeed = :avgSpeed WHERE id = :subjectId")
    suspend fun updateReadingSpeed(subjectId: Long, speed: Double, avgSpeed: Double)
    
    @Query("SELECT SUM(totalPages - completedPages) FROM subjects WHERE isActive = 1")
    suspend fun getTotalRemainingPages(): Int?
    
    @Query("SELECT * FROM subjects WHERE targetDate < :date AND isActive = 1 AND completedPages < totalPages")
    fun getOverdueSubjects(date: Long): Flow<List<SubjectEntity>>
}