package com.focusor.app.data.repository

import com.focusor.app.data.local.dao.SubjectDao
import com.focusor.app.data.local.dao.StudySessionDao
import com.focusor.app.data.local.entity.SubjectEntity
import com.focusor.app.data.local.entity.StudySessionEntity
import com.focusor.app.domain.model.Subject
import com.focusor.app.domain.model.StudySession
import com.focusor.app.domain.model.StudyPlan
import com.focusor.app.domain.model.StudyRecommendation
import com.focusor.app.domain.repository.EducationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

class EducationRepositoryImpl @Inject constructor(
    private val subjectDao: SubjectDao,
    private val studySessionDao: StudySessionDao
) : EducationRepository {
    
    // Subject operations
    override fun getAllActiveSubjects(): Flow<List<Subject>> {
        return subjectDao.getAllActiveSubjects().map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override suspend fun getSubjectById(subjectId: Long): Subject? {
        return subjectDao.getSubjectById(subjectId)?.toDomain()
    }
    
    override suspend fun insertSubject(subject: Subject): Long {
        return subjectDao.insertSubject(subject.toEntity())
    }
    
    override suspend fun updateSubject(subject: Subject) {
        subjectDao.updateSubject(subject.toEntity())
    }
    
    override suspend fun deleteSubject(subject: Subject) {
        subjectDao.deleteSubject(subject.toEntity())
    }
    
    override suspend fun updateCurrentPage(subjectId: Long, currentPage: Int) {
        subjectDao.updateCurrentPage(subjectId, currentPage, LocalDateTime.now())
    }
    
    override suspend fun updateReadingSpeed(subjectId: Long, readingSpeed: Double) {
        subjectDao.updateReadingSpeed(subjectId, readingSpeed, LocalDateTime.now())
    }
    
    override suspend fun updateDailyPages(subjectId: Long, dailyPages: Int) {
        subjectDao.updateDailyPages(subjectId, dailyPages, LocalDateTime.now())
    }
    
    // Study session operations
    override fun getSessionsBySubject(subjectId: Long): Flow<List<StudySession>> {
        return studySessionDao.getSessionsBySubject(subjectId).map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override suspend fun insertSession(session: StudySession): Long {
        return studySessionDao.insertSession(session.toEntity())
    }
    
    override suspend fun updateSession(session: StudySession) {
        studySessionDao.updateSession(session.toEntity())
    }
    
    override suspend fun deleteSession(session: StudySession) {
        studySessionDao.deleteSession(session.toEntity())
    }
    
    override suspend fun getAverageReadingSpeed(subjectId: Long, startDate: LocalDateTime): Double? {
        return studySessionDao.getAverageReadingSpeed(subjectId, startDate)
    }
    
    override suspend fun getRecentSessions(subjectId: Long, limit: Int): List<StudySession> {
        return studySessionDao.getRecentSessions(subjectId, limit).map { it.toDomain() }
    }
    
    override suspend fun getTotalStudyTime(subjectId: Long, startDate: LocalDateTime): Int? {
        return studySessionDao.getTotalStudyTime(subjectId, startDate)
    }
    
    override suspend fun getTotalPagesRead(subjectId: Long, startDate: LocalDateTime): Int? {
        return studySessionDao.getTotalPagesRead(subjectId, startDate)
    }
    
    // Study plan operations
    override suspend fun getStudyPlan(totalAvailableHours: Double): StudyPlan {
        val subjects = getAllActiveSubjects().map { it }.first()
        return StudyPlan(subjects = subjects, totalAvailableHours = totalAvailableHours)
    }
    
    override suspend fun getStudyRecommendations(): List<StudyRecommendation> {
        val studyPlan = getStudyPlan()
        return studyPlan.getStudyRecommendations()
    }
    
    override suspend fun getSubjectsDueByDate(date: LocalDate): List<Subject> {
        return subjectDao.getSubjectsDueByDate(date).map { it.toDomain() }.first()
    }
    
    override suspend fun getSubjectsInDateRange(startDate: LocalDate, endDate: LocalDate): List<Subject> {
        return subjectDao.getSubjectsInDateRange(startDate, endDate).map { it.toDomain() }.first()
    }
    
    // Analytics
    override suspend fun getActiveSubjectCount(): Int {
        return subjectDao.getActiveSubjectCount()
    }
    
    override suspend fun getTotalRemainingPages(): Int {
        return subjectDao.getTotalRemainingPages() ?: 0
    }
    
    override suspend fun getRecentSessionsAllSubjects(limit: Int): List<StudySession> {
        return studySessionDao.getRecentSessionsAllSubjects(limit).map { it.toDomain() }.first()
    }
    
    // Smart calculations
    override suspend fun calculateOptimalDailyPages(subjectId: Long, targetHoursPerDay: Double): Int {
        val subject = getSubjectById(subjectId) ?: return 0
        return subject.calculateOptimalDailyPages(targetHoursPerDay)
    }
    
    override suspend fun calculateCompletionDate(subjectId: Long, hoursPerDay: Double): LocalDate? {
        val subject = getSubjectById(subjectId) ?: return null
        return subject.calculateCompletionDateWithHours(hoursPerDay)
    }
    
    override suspend fun calculateBacklogRecoveryTime(subjectId: Long, extraHoursPerDay: Double): Int {
        val subject = getSubjectById(subjectId) ?: return 0
        return subject.calculateBacklogRecoveryTime(extraHoursPerDay)
    }
    
    override suspend fun updateSubjectReadingSpeedFromSessions(subjectId: Long) {
        val weekAgo = LocalDateTime.now().minusDays(7)
        val averageSpeed = getAverageReadingSpeed(subjectId, weekAgo)
        averageSpeed?.let { speed ->
            updateReadingSpeed(subjectId, speed)
        }
    }
    
    // Extension functions for mapping
    private fun SubjectEntity.toDomain(): Subject {
        return Subject(
            id = id,
            name = name,
            source = source,
            totalPages = totalPages,
            targetDate = targetDate,
            currentPage = currentPage,
            dailyPages = dailyPages,
            readingSpeed = readingSpeed,
            priority = priority,
            isActive = isActive,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
    
    private fun Subject.toEntity(): SubjectEntity {
        return SubjectEntity(
            id = id,
            name = name,
            source = source,
            totalPages = totalPages,
            targetDate = targetDate,
            currentPage = currentPage,
            dailyPages = dailyPages,
            readingSpeed = readingSpeed,
            priority = priority,
            isActive = isActive,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
    
    private fun StudySessionEntity.toDomain(): StudySession {
        return StudySession(
            id = id,
            subjectId = subjectId,
            startPage = startPage,
            endPage = endPage,
            durationMinutes = durationMinutes,
            pagesRead = pagesRead,
            readingSpeed = readingSpeed,
            sessionDate = sessionDate,
            notes = notes
        )
    }
    
    private fun StudySession.toEntity(): StudySessionEntity {
        return StudySessionEntity(
            id = id,
            subjectId = subjectId,
            startPage = startPage,
            endPage = endPage,
            durationMinutes = durationMinutes,
            pagesRead = pagesRead,
            readingSpeed = readingSpeed,
            sessionDate = sessionDate,
            notes = notes
        )
    }
}