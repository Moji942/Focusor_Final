package com.focusor.di

import android.content.Context
import androidx.room.Room
import com.focusor.data.database.FocusorDatabase
import com.focusor.data.database.dao.*
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
            context.applicationContext,
            FocusorDatabase::class.java,
            FocusorDatabase.DATABASE_NAME
        )
        .fallbackToDestructiveMigration()
        .build()
    }
    
    @Provides
    fun provideEducationDao(database: FocusorDatabase): EducationDao {
        return database.educationDao()
    }
    
    @Provides
    fun provideFinanceDao(database: FocusorDatabase): FinanceDao {
        return database.financeDao()
    }
    
    @Provides
    fun provideNotesDao(database: FocusorDatabase): NotesDao {
        return database.notesDao()
    }
    
    @Provides
    fun provideAIDao(database: FocusorDatabase): AIDao {
        return database.aiDao()
    }
}