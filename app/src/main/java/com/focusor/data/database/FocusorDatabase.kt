package com.focusor.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.focusor.data.database.converters.Converters
import com.focusor.data.database.dao.*
import com.focusor.data.database.entities.*

@Database(
    entities = [
        EducationSubject::class,
        StudySession::class,
        FinanceCard::class,
        FinanceLoan::class,
        FinanceGoal::class,
        Note::class,
        AIConversation::class,
        AIMessage::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class FocusorDatabase : RoomDatabase() {
    
    abstract fun educationDao(): EducationDao
    abstract fun financeDao(): FinanceDao
    abstract fun notesDao(): NotesDao
    abstract fun aiDao(): AIDao
    
    companion object {
        const val DATABASE_NAME = "focusor_database.db"
        
        @Volatile
        private var INSTANCE: FocusorDatabase? = null
        
        fun getInstance(context: Context): FocusorDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FocusorDatabase::class.java,
                    DATABASE_NAME
                )
                .fallbackToDestructiveMigration() // For development only
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}