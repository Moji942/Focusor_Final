package com.focusor.studyplanner.di

import android.content.Context
import androidx.room.Room
import com.focusor.studyplanner.data.dao.*
import com.focusor.studyplanner.data.database.FocusorDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): FocusorDatabase {
        return Room.databaseBuilder(
            context,
            FocusorDatabase::class.java,
            FocusorDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }
    
    @Provides
    fun provideSubjectDao(database: FocusorDatabase): SubjectDao = database.subjectDao()
    
    @Provides
    fun provideStudySessionDao(database: FocusorDatabase): StudySessionDao = database.studySessionDao()
    
    @Provides
    fun provideCardDao(database: FocusorDatabase): CardDao = database.cardDao()
    
    @Provides
    fun provideTransactionDao(database: FocusorDatabase): TransactionDao = database.transactionDao()
    
    @Provides
    fun provideInstallmentDao(database: FocusorDatabase): InstallmentDao = database.installmentDao()
    
    @Provides
    fun provideNoteDao(database: FocusorDatabase): NoteDao = database.noteDao()
    
    @Provides
    fun provideAIProviderDao(database: FocusorDatabase): AIProviderDao = database.aiProviderDao()
}