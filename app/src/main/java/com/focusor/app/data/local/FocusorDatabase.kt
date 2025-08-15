package com.focusor.app.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.focusor.app.data.local.dao.*
import com.focusor.app.data.local.entity.*
import com.focusor.app.data.local.converter.*

@Database(
    entities = [
        SubjectEntity::class,
        StudySessionEntity::class,
        CardEntity::class,
        TransactionEntity::class,
        LoanEntity::class,
        NoteEntity::class,
        AiConversationEntity::class,
        AiMessageEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    LocalDateConverter::class,
    LocalDateTimeConverter::class,
    BigDecimalConverter::class,
    TransactionTypeConverter::class
)
abstract class FocusorDatabase : RoomDatabase() {
    
    abstract fun subjectDao(): SubjectDao
    abstract fun studySessionDao(): StudySessionDao
    abstract fun cardDao(): CardDao
    abstract fun transactionDao(): TransactionDao
    abstract fun loanDao(): LoanDao
    abstract fun noteDao(): NoteDao
    abstract fun aiConversationDao(): AiConversationDao
    abstract fun aiMessageDao(): AiMessageDao
    
    companion object {
        @Volatile
        private var INSTANCE: FocusorDatabase? = null
        
        fun getDatabase(context: Context): FocusorDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FocusorDatabase::class.java,
                    "focusor_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}