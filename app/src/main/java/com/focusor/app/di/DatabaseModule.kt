package com.focusor.app.di

import android.content.Context
import com.focusor.app.data.local.FocusorDatabase
import com.focusor.app.data.local.dao.*
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
        return FocusorDatabase.getDatabase(context)
    }
    
    @Provides
    fun provideSubjectDao(database: FocusorDatabase): SubjectDao {
        return database.subjectDao()
    }
    
    @Provides
    fun provideStudySessionDao(database: FocusorDatabase): StudySessionDao {
        return database.studySessionDao()
    }
    
    @Provides
    fun provideCardDao(database: FocusorDatabase): CardDao {
        return database.cardDao()
    }
    
    @Provides
    fun provideTransactionDao(database: FocusorDatabase): TransactionDao {
        return database.transactionDao()
    }
    
    @Provides
    fun provideLoanDao(database: FocusorDatabase): LoanDao {
        return database.loanDao()
    }
    
    @Provides
    fun provideNoteDao(database: FocusorDatabase): NoteDao {
        return database.noteDao()
    }
    
    @Provides
    fun provideAiConversationDao(database: FocusorDatabase): AiConversationDao {
        return database.aiConversationDao()
    }
    
    @Provides
    fun provideAiMessageDao(database: FocusorDatabase): AiMessageDao {
        return database.aiMessageDao()
    }
}