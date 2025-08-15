package com.focusor.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.focusor.app.data.local.entity.StudySessionEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface StudySessionDao {
    
    @Query("SELECT * FROM study_sessions WHERE subjectId = :subjectId ORDER BY sessionDate DESC")
    fun getSessionsBySubject(subjectId: Long): Flow<List<StudySessionEntity>>
    
    @Query("SELECT * FROM study_sessions WHERE subjectId = :subjectId AND sessionDate >= :startDate ORDER BY sessionDate DESC")
    fun getSessionsBySubjectAndDate(subjectId: Long, startDate: LocalDateTime): Flow<List<StudySessionEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: StudySessionEntity): Long
    
    @Update
    suspend fun updateSession(session: StudySessionEntity)
    
    @Delete
    suspend fun deleteSession(session: StudySessionEntity)
    
    @Query("SELECT AVG(readingSpeed) FROM study_sessions WHERE subjectId = :subjectId AND sessionDate >= :startDate")
    suspend fun getAverageReadingSpeed(subjectId: Long, startDate: LocalDateTime): Double?
    
    @Query("SELECT * FROM study_sessions WHERE subjectId = :subjectId ORDER BY sessionDate DESC LIMIT :limit")
    suspend fun getRecentSessions(subjectId: Long, limit: Int): List<StudySessionEntity>
    
    @Query("SELECT SUM(durationMinutes) FROM study_sessions WHERE subjectId = :subjectId AND sessionDate >= :startDate")
    suspend fun getTotalStudyTime(subjectId: Long, startDate: LocalDateTime): Int?
    
    @Query("SELECT SUM(pagesRead) FROM study_sessions WHERE subjectId = :subjectId AND sessionDate >= :startDate")
    suspend fun getTotalPagesRead(subjectId: Long, startDate: LocalDateTime): Int?
    
    @Query("SELECT * FROM study_sessions ORDER BY sessionDate DESC LIMIT :limit")
    fun getRecentSessionsAllSubjects(limit: Int): Flow<List<StudySessionEntity>>
}