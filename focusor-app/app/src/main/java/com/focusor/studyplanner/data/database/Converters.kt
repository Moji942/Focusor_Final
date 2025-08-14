package com.focusor.studyplanner.data.database

import androidx.room.TypeConverter
import com.focusor.studyplanner.data.entity.InstallmentStatus
import com.focusor.studyplanner.data.entity.TransactionType
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class Converters {
    
    @TypeConverter
    fun fromBigDecimal(value: BigDecimal?): String? {
        return value?.toString()
    }
    
    @TypeConverter
    fun toBigDecimal(value: String?): BigDecimal? {
        return value?.let { BigDecimal(it) }
    }
    
    @TypeConverter
    fun fromLocalDate(date: LocalDate?): Long? {
        return date?.toEpochDay()
    }
    
    @TypeConverter
    fun toLocalDate(value: Long?): LocalDate? {
        return value?.let { LocalDate.ofEpochDay(it) }
    }
    
    @TypeConverter
    fun fromLocalDateTime(dateTime: LocalDateTime?): Long? {
        return dateTime?.toEpochSecond(java.time.ZoneOffset.UTC, 0)
    }
    
    @TypeConverter
    fun toLocalDateTime(value: Long?): LocalDateTime? {
        return value?.let { 
            LocalDateTime.ofEpochSecond(it, 0, java.time.ZoneOffset.UTC)
        }
    }
    
    @TypeConverter
    fun fromLocalTime(time: LocalTime?): Int? {
        return time?.toSecondOfDay()
    }
    
    @TypeConverter
    fun toLocalTime(value: Int?): LocalTime? {
        return value?.let { LocalTime.ofSecondOfDay(it.toLong()) }
    }
    
    @TypeConverter
    fun fromTransactionType(type: TransactionType): String {
        return type.name
    }
    
    @TypeConverter
    fun toTransactionType(value: String): TransactionType {
        return TransactionType.valueOf(value)
    }
    
    @TypeConverter
    fun fromInstallmentStatus(status: InstallmentStatus): String {
        return status.name
    }
    
    @TypeConverter
    fun toInstallmentStatus(value: String): InstallmentStatus {
        return InstallmentStatus.valueOf(value)
    }
}