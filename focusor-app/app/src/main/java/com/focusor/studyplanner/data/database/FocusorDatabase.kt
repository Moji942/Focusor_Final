package com.focusor.studyplanner.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.focusor.studyplanner.data.dao.*
import com.focusor.studyplanner.data.entity.*

@Database(
    entities = [
        SubjectEntity::class,
        StudySessionEntity::class,
        CardEntity::class,
        TransactionEntity::class,
        InstallmentEntity::class,
        NoteEntity::class,
        AIProviderEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class FocusorDatabase : RoomDatabase() {
    
    abstract fun subjectDao(): SubjectDao
    abstract fun studySessionDao(): StudySessionDao
    abstract fun cardDao(): CardDao
    abstract fun transactionDao(): TransactionDao
    abstract fun installmentDao(): InstallmentDao
    abstract fun noteDao(): NoteDao
    abstract fun aiProviderDao(): AIProviderDao
    
    companion object {
        const val DATABASE_NAME = "focusor_database"
    }
}