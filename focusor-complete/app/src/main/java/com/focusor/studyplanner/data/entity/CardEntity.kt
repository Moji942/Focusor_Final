package com.focusor.studyplanner.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.time.LocalDate

@Entity(tableName = "cards")
data class CardEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val bankName: String,
    val cardNumberEncrypted: String, // Last 4 digits only stored
    val iban: String? = null,
    val accountNumber: String? = null,
    val currentBalance: BigDecimal,
    val currency: String = "IRR",
    val colorCode: String = "#1976D2",
    val isActive: Boolean = true,
    val expiryDate: LocalDate? = null,
    val cardType: String = "DEBIT", // DEBIT, CREDIT, PREPAID
    val lastUpdated: Long = System.currentTimeMillis(),
    val createdAt: Long = System.currentTimeMillis()
)