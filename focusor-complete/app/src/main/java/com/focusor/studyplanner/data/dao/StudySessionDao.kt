package com.focusor.studyplanner.data.dao

import androidx.room.*
import com.focusor.studyplanner.data.entity.StudySessionEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface StudySessionDao {
    
    @Insert
    suspend fun insertSession(session: StudySessionEntity): Long
    
    @Update
    suspend fun updateSession(session: StudySessionEntity)
    
    @Delete
    suspend fun deleteSession(session: StudySessionEntity)
    
    @Query("SELECT * FROM study_sessions WHERE subjectId = :subjectId ORDER BY date DESC, startTime DESC")
    fun getSessionsBySubject(subjectId: Long): Flow<List<StudySessionEntity>>
    
    @Query("SELECT * FROM study_sessions WHERE date = :date ORDER BY startTime ASC")
    fun getSessionsByDate(date: LocalDate): Flow<List<StudySessionEntity>>
    
    @Query("SELECT * FROM study_sessions WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getSessionsInDateRange(startDate: LocalDate, endDate: LocalDate): Flow<List<StudySessionEntity>>
    
    @Query("SELECT AVG(actualPages * 1.0 / actualHours) FROM study_sessions WHERE subjectId = :subjectId AND date >= :fromDate")
    suspend fun getAverageReadingSpeed(subjectId: Long, fromDate: LocalDate): Double?
    
    @Query("SELECT SUM(actualPages) FROM study_sessions WHERE subjectId = :subjectId")
    suspend fun getTotalPagesRead(subjectId: Long): Int?
    
    @Query("SELECT SUM(actualHours) FROM study_sessions WHERE subjectId = :subjectId")
    suspend fun getTotalHoursStudied(subjectId: Long): Double?
}