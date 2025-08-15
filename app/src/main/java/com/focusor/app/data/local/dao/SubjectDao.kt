package com.focusor.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.focusor.app.data.local.entity.SubjectEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface SubjectDao {
    
    @Query("SELECT * FROM subjects WHERE isActive = 1 ORDER BY priority DESC, targetDate ASC")
    fun getAllActiveSubjects(): Flow<List<SubjectEntity>>
    
    @Query("SELECT * FROM subjects WHERE id = :subjectId")
    suspend fun getSubjectById(subjectId: Long): SubjectEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubject(subject: SubjectEntity): Long
    
    @Update
    suspend fun updateSubject(subject: SubjectEntity)
    
    @Delete
    suspend fun deleteSubject(subject: SubjectEntity)
    
    @Query("UPDATE subjects SET currentPage = :currentPage, updatedAt = :updatedAt WHERE id = :subjectId")
    suspend fun updateCurrentPage(subjectId: Long, currentPage: Int, updatedAt: java.time.LocalDateTime)
    
    @Query("UPDATE subjects SET readingSpeed = :readingSpeed, updatedAt = :updatedAt WHERE id = :subjectId")
    suspend fun updateReadingSpeed(subjectId: Long, readingSpeed: Double, updatedAt: java.time.LocalDateTime)
    
    @Query("UPDATE subjects SET dailyPages = :dailyPages, updatedAt = :updatedAt WHERE id = :subjectId")
    suspend fun updateDailyPages(subjectId: Long, dailyPages: Int, updatedAt: java.time.LocalDateTime)
    
    @Query("SELECT * FROM subjects WHERE targetDate <= :date AND isActive = 1 ORDER BY targetDate ASC")
    fun getSubjectsDueByDate(date: LocalDate): Flow<List<SubjectEntity>>
    
    @Query("SELECT * FROM subjects WHERE targetDate BETWEEN :startDate AND :endDate AND isActive = 1 ORDER BY targetDate ASC")
    fun getSubjectsInDateRange(startDate: LocalDate, endDate: LocalDate): Flow<List<SubjectEntity>>
    
    @Query("SELECT COUNT(*) FROM subjects WHERE isActive = 1")
    suspend fun getActiveSubjectCount(): Int
    
    @Query("SELECT SUM(totalPages - currentPage) FROM subjects WHERE isActive = 1")
    suspend fun getTotalRemainingPages(): Int?
}