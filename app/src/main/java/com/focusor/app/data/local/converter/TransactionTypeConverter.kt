package com.focusor.app.data.local.converter

import androidx.room.TypeConverter
import com.focusor.app.data.local.entity.TransactionType

class TransactionTypeConverter {
    @TypeConverter
    fun fromTransactionType(value: TransactionType?): String? {
        return value?.name
    }
    
    @TypeConverter
    fun toTransactionType(value: String?): TransactionType? {
        return value?.let { TransactionType.valueOf(it) }
    }
}