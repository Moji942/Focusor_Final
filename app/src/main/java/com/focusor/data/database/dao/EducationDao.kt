package com.focusor.data.database.dao

import androidx.room.*
import com.focusor.data.database.entities.EducationSubject
import com.focusor.data.database.entities.StudySession
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface EducationDao {
    
    // EducationSubject operations
    @Query("SELECT * FROM education_subjects ORDER BY priority DESC, targetDate ASC")
    fun getAllSubjects(): Flow<List<EducationSubject>>
    
    @Query("SELECT * FROM education_subjects WHERE id = :id")
    suspend fun getSubjectById(id: Long): EducationSubject?
    
    @Query("SELECT * FROM education_subjects WHERE isCompleted = 0 ORDER BY priority DESC, targetDate ASC")
    fun getActiveSubjects(): Flow<List<EducationSubject>>
    
    @Query("SELECT * FROM education_subjects WHERE isCompleted = 1 ORDER BY updatedAt DESC")
    fun getCompletedSubjects(): Flow<List<EducationSubject>>
    
    @Query("SELECT * FROM education_subjects WHERE targetDate < :currentDate AND isCompleted = 0")
    suspend fun getOverdueSubjects(currentDate: Date = Date()): List<EducationSubject>
    
    @Query("SELECT * FROM education_subjects WHERE priority >= :minPriority ORDER BY priority DESC")
    fun getSubjectsByPriority(minPriority: Int): Flow<List<EducationSubject>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubject(subject: EducationSubject): Long
    
    @Update
    suspend fun updateSubject(subject: EducationSubject)
    
    @Delete
    suspend fun deleteSubject(subject: EducationSubject)
    
    @Query("UPDATE education_subjects SET currentPage = :currentPage, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updateProgress(id: Long, currentPage: Int, updatedAt: Date = Date())
    
    @Query("UPDATE education_subjects SET isCompleted = :isCompleted, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updateCompletion(id: Long, isCompleted: Boolean, updatedAt: Date = Date())
    
    @Query("UPDATE education_subjects SET readingSpeed = :readingSpeed, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updateReadingSpeed(id: Long, readingSpeed: String, updatedAt: Date = Date()) // String for BigDecimal
    
    // StudySession operations
    @Query("SELECT * FROM study_sessions WHERE subjectId = :subjectId ORDER BY sessionDate DESC")
    fun getSessionsForSubject(subjectId: Long): Flow<List<StudySession>>
    
    @Query("SELECT * FROM study_sessions WHERE subjectId = :subjectId ORDER BY sessionDate DESC LIMIT 7")
    suspend fun getRecentSessionsForSubject(subjectId: Long): List<StudySession>
    
    @Query("SELECT * FROM study_sessions WHERE sessionDate BETWEEN :startDate AND :endDate ORDER BY sessionDate DESC")
    fun getSessionsInDateRange(startDate: Date, endDate: Date): Flow<List<StudySession>>
    
    @Query("SELECT * FROM study_sessions WHERE sessionDate = :date ORDER BY startTime DESC")
    suspend fun getSessionsForDate(date: Date): List<StudySession>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: StudySession): Long
    
    @Update
    suspend fun updateSession(session: StudySession)
    
    @Delete
    suspend fun deleteSession(session: StudySession)
    
    // Analytics queries
    @Query("""
        SELECT AVG(
            CASE 
                WHEN (julianday(endTime) - julianday(startTime)) * 24 > 0 
                THEN (endPage - startPage) / ((julianday(endTime) - julianday(startTime)) * 24)
                ELSE 0 
            END
        ) 
        FROM study_sessions 
        WHERE subjectId = :subjectId 
        AND sessionDate >= :since
        AND endPage > startPage
    """)
    suspend fun getAverageReadingSpeed(subjectId: Long, since: Date): Double?
    
    @Query("""
        SELECT SUM(endPage - startPage) 
        FROM study_sessions 
        WHERE subjectId = :subjectId 
        AND sessionDate BETWEEN :startDate AND :endDate
    """)
    suspend fun getTotalPagesRead(subjectId: Long, startDate: Date, endDate: Date): Int?
    
    @Query("""
        SELECT SUM((julianday(endTime) - julianday(startTime)) * 24 * 60) 
        FROM study_sessions 
        WHERE subjectId = :subjectId 
        AND sessionDate BETWEEN :startDate AND :endDate
    """)
    suspend fun getTotalStudyTimeMinutes(subjectId: Long, startDate: Date, endDate: Date): Double?
    
    @Query("SELECT COUNT(*) FROM education_subjects WHERE isCompleted = 0")
    suspend fun getActiveSubjectCount(): Int
    
    @Query("SELECT COUNT(*) FROM education_subjects WHERE isCompleted = 1")
    suspend fun getCompletedSubjectCount(): Int
    
    @Query("""
        SELECT COUNT(*) FROM education_subjects 
        WHERE targetDate < :currentDate AND isCompleted = 0
    """)
    suspend fun getOverdueSubjectCount(currentDate: Date = Date()): Int
    
    // Dashboard queries
    @Query("""
        SELECT SUM(endPage - startPage) 
        FROM study_sessions 
        WHERE sessionDate = :today
    """)
    suspend fun getTodayPagesRead(today: Date): Int?
    
    @Query("""
        SELECT SUM((julianday(endTime) - julianday(startTime)) * 24 * 60) 
        FROM study_sessions 
        WHERE sessionDate = :today
    """)
    suspend fun getTodayStudyTimeMinutes(today: Date): Double?
    
    @Query("""
        SELECT es.name, es.targetDate, 
               (es.totalPages - es.currentPage) as remainingPages,
               (julianday(es.targetDate) - julianday('now')) as daysRemaining
        FROM education_subjects es 
        WHERE es.isCompleted = 0 
        AND es.targetDate > date('now')
        ORDER BY daysRemaining ASC 
        LIMIT 5
    """)
    suspend fun getUpcomingDeadlines(): List<SubjectDeadline>
    
    // Search functionality
    @Query("""
        SELECT * FROM education_subjects 
        WHERE name LIKE '%' || :query || '%' 
        OR source LIKE '%' || :query || '%' 
        OR notes LIKE '%' || :query || '%'
        ORDER BY 
            CASE WHEN name LIKE '%' || :query || '%' THEN 1 ELSE 2 END,
            priority DESC
    """)
    fun searchSubjects(query: String): Flow<List<EducationSubject>>
}

data class SubjectDeadline(
    val name: String,
    val targetDate: Date,
    val remainingPages: Int,
    val daysRemaining: Double
)