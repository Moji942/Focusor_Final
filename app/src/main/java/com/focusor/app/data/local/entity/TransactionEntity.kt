package com.focusor.app.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = CardEntity::class,
            parentColumns = ["id"],
            childColumns = ["cardId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val cardId: Long? = null, // null for cash transactions
    val amount: BigDecimal,
    val description: String,
    val category: String,
    val transactionType: TransactionType, // INCOME, EXPENSE, TRANSFER
    val transactionDate: LocalDateTime = LocalDateTime.now(),
    val isRecurring: Boolean = false,
    val recurringInterval: String? = null, // "monthly", "weekly", etc.
    val notes: String = ""
)

enum class TransactionType {
    INCOME, EXPENSE, TRANSFER
}