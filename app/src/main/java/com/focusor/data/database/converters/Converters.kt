package com.focusor.data.database.converters

import androidx.room.TypeConverter
import com.focusor.data.database.entities.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.math.BigDecimal
import java.util.Date

class Converters {
    
    // Date converters
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }
    
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
    
    // BigDecimal converters
    @TypeConverter
    fun fromBigDecimal(value: BigDecimal?): String? {
        return value?.toString()
    }
    
    @TypeConverter
    fun toBigDecimal(value: String?): BigDecimal? {
        return value?.let { BigDecimal(it) }
    }
    
    // List<String> converters for tags
    @TypeConverter
    fun fromStringList(value: List<String>?): String? {
        return value?.let { Gson().toJson(it) }
    }
    
    @TypeConverter
    fun toStringList(value: String?): List<String> {
        return value?.let {
            val listType = object : TypeToken<List<String>>() {}.type
            Gson().fromJson(it, listType)
        } ?: emptyList()
    }
    
    // CardType converters
    @TypeConverter
    fun fromCardType(value: CardType?): String? {
        return value?.name
    }
    
    @TypeConverter
    fun toCardType(value: String?): CardType? {
        return value?.let { CardType.valueOf(it) }
    }
    
    // LoanType converters
    @TypeConverter
    fun fromLoanType(value: LoanType?): String? {
        return value?.name
    }
    
    @TypeConverter
    fun toLoanType(value: String?): LoanType? {
        return value?.let { LoanType.valueOf(it) }
    }
    
    // GoalType converters
    @TypeConverter
    fun fromGoalType(value: GoalType?): String? {
        return value?.name
    }
    
    @TypeConverter
    fun toGoalType(value: String?): GoalType? {
        return value?.let { GoalType.valueOf(it) }
    }
    
    // NotePriority converters
    @TypeConverter
    fun fromNotePriority(value: NotePriority?): String? {
        return value?.name
    }
    
    @TypeConverter
    fun toNotePriority(value: String?): NotePriority? {
        return value?.let { NotePriority.valueOf(it) }
    }
    
    // AIProvider converters
    @TypeConverter
    fun fromAIProvider(value: AIProvider?): String? {
        return value?.name
    }
    
    @TypeConverter
    fun toAIProvider(value: String?): AIProvider? {
        return value?.let { AIProvider.valueOf(it) }
    }
    
    // MessageRole converters
    @TypeConverter
    fun fromMessageRole(value: MessageRole?): String? {
        return value?.name
    }
    
    @TypeConverter
    fun toMessageRole(value: String?): MessageRole? {
        return value?.let { MessageRole.valueOf(it) }
    }
}