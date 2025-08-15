package com.focusor.studyplanner.domain.repository

import com.focusor.studyplanner.data.entity.StudySessionEntity
import com.focusor.studyplanner.data.entity.SubjectEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface EducationRepository {
    fun getAllActiveSubjects(): Flow<List<SubjectEntity>>
    fun getAllSubjects(): Flow<List<SubjectEntity>>
    suspend fun getSubjectById(id: Long): SubjectEntity?
    suspend fun insertSubject(subject: SubjectEntity): Long
    suspend fun updateSubject(subject: SubjectEntity)
    suspend fun deleteSubject(subject: SubjectEntity)
    suspend fun updateProgress(subjectId: Long, completedPages: Int)
    suspend fun updateReadingSpeed(subjectId: Long, speed: Double, avgSpeed: Double)
    suspend fun insertStudySession(session: StudySessionEntity): Long
    fun getSessionsBySubject(subjectId: Long): Flow<List<StudySessionEntity>>
    fun getSessionsByDate(date: LocalDate): Flow<List<StudySessionEntity>>
    suspend fun getTotalPagesRead(subjectId: Long): Int
    suspend fun getTotalHoursStudied(subjectId: Long): Double
}