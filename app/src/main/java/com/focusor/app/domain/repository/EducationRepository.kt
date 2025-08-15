package com.focusor.app.domain.repository

import com.focusor.app.domain.model.Subject
import com.focusor.app.domain.model.StudySession
import com.focusor.app.domain.model.StudyPlan
import com.focusor.app.domain.model.StudyRecommendation
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalDateTime

interface EducationRepository {
    
    // Subject operations
    fun getAllActiveSubjects(): Flow<List<Subject>>
    suspend fun getSubjectById(subjectId: Long): Subject?
    suspend fun insertSubject(subject: Subject): Long
    suspend fun updateSubject(subject: Subject)
    suspend fun deleteSubject(subject: Subject)
    suspend fun updateCurrentPage(subjectId: Long, currentPage: Int)
    suspend fun updateReadingSpeed(subjectId: Long, readingSpeed: Double)
    suspend fun updateDailyPages(subjectId: Long, dailyPages: Int)
    
    // Study session operations
    fun getSessionsBySubject(subjectId: Long): Flow<List<StudySession>>
    suspend fun insertSession(session: StudySession): Long
    suspend fun updateSession(session: StudySession)
    suspend fun deleteSession(session: StudySession)
    suspend fun getAverageReadingSpeed(subjectId: Long, startDate: LocalDateTime): Double?
    suspend fun getRecentSessions(subjectId: Long, limit: Int): List<StudySession>
    suspend fun getTotalStudyTime(subjectId: Long, startDate: LocalDateTime): Int?
    suspend fun getTotalPagesRead(subjectId: Long, startDate: LocalDateTime): Int?
    
    // Study plan operations
    suspend fun getStudyPlan(totalAvailableHours: Double = 24.0): StudyPlan
    suspend fun getStudyRecommendations(): List<StudyRecommendation>
    suspend fun getSubjectsDueByDate(date: LocalDate): List<Subject>
    suspend fun getSubjectsInDateRange(startDate: LocalDate, endDate: LocalDate): List<Subject>
    
    // Analytics
    suspend fun getActiveSubjectCount(): Int
    suspend fun getTotalRemainingPages(): Int
    suspend fun getRecentSessionsAllSubjects(limit: Int): List<StudySession>
    
    // Smart calculations
    suspend fun calculateOptimalDailyPages(subjectId: Long, targetHoursPerDay: Double): Int
    suspend fun calculateCompletionDate(subjectId: Long, hoursPerDay: Double): LocalDate?
    suspend fun calculateBacklogRecoveryTime(subjectId: Long, extraHoursPerDay: Double): Int
    suspend fun updateSubjectReadingSpeedFromSessions(subjectId: Long)
}