package com.focusor.studyplanner.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
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
    ],
    indices = [Index("cardId"), Index("date"), Index("category")]
)
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val cardId: Long,
    val amount: BigDecimal,
    val type: TransactionType,
    val category: String,
    val description: String,
    val date: LocalDateTime,
    val isRecurring: Boolean = false,
    val recurringId: Long? = null,
    val tags: String? = null, // JSON array of tags
    val attachmentPath: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

enum class TransactionType {
    INCOME,
    EXPENSE,
    TRANSFER
}