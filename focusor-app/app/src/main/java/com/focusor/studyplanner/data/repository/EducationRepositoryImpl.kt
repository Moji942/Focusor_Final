package com.focusor.studyplanner.data.repository

import com.focusor.studyplanner.data.dao.StudySessionDao
import com.focusor.studyplanner.data.dao.SubjectDao
import com.focusor.studyplanner.data.entity.StudySessionEntity
import com.focusor.studyplanner.data.entity.SubjectEntity
import com.focusor.studyplanner.domain.repository.EducationRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EducationRepositoryImpl @Inject constructor(
    private val subjectDao: SubjectDao,
    private val studySessionDao: StudySessionDao
) : EducationRepository {
    
    override fun getAllActiveSubjects(): Flow<List<SubjectEntity>> {
        return subjectDao.getAllActiveSubjects()
    }
    
    override fun getAllSubjects(): Flow<List<SubjectEntity>> {
        return subjectDao.getAllSubjects()
    }
    
    override suspend fun getSubjectById(id: Long): SubjectEntity? {
        return subjectDao.getSubjectById(id)
    }
    
    override suspend fun insertSubject(subject: SubjectEntity): Long {
        return subjectDao.insertSubject(subject)
    }
    
    override suspend fun updateSubject(subject: SubjectEntity) {
        subjectDao.updateSubject(subject)
    }
    
    override suspend fun deleteSubject(subject: SubjectEntity) {
        subjectDao.deleteSubject(subject)
    }
    
    override suspend fun updateProgress(subjectId: Long, completedPages: Int) {
        subjectDao.updateProgress(subjectId, completedPages)
    }
    
    override suspend fun updateReadingSpeed(subjectId: Long, speed: Double, avgSpeed: Double) {
        subjectDao.updateReadingSpeed(subjectId, speed, avgSpeed)
    }
    
    override suspend fun insertStudySession(session: StudySessionEntity): Long {
        return studySessionDao.insertSession(session)
    }
    
    override fun getSessionsBySubject(subjectId: Long): Flow<List<StudySessionEntity>> {
        return studySessionDao.getSessionsBySubject(subjectId)
    }
    
    override fun getSessionsByDate(date: LocalDate): Flow<List<StudySessionEntity>> {
        return studySessionDao.getSessionsByDate(date)
    }
    
    override suspend fun getTotalPagesRead(subjectId: Long): Int {
        return studySessionDao.getTotalPagesRead(subjectId) ?: 0
    }
    
    override suspend fun getTotalHoursStudied(subjectId: Long): Double {
        return studySessionDao.getTotalHoursStudied(subjectId) ?: 0.0
    }
}