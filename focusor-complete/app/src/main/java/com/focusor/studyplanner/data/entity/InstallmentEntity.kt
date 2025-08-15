package com.focusor.studyplanner.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.time.LocalDate

@Entity(
    tableName = "installments",
    foreignKeys = [
        ForeignKey(
            entity = CardEntity::class,
            parentColumns = ["id"],
            childColumns = ["sourceCardId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("sourceCardId"), Index("startDate"), Index("status")]
)
data class InstallmentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val totalAmount: BigDecimal,
    val installmentCount: Int,
    val paidCount: Int = 0,
    val monthlyPayment: BigDecimal,
    val sourceCardId: Long,
    val startDate: LocalDate,
    val interestRate: Double = 0.0,
    val status: InstallmentStatus = InstallmentStatus.ACTIVE,
    val category: String,
    val notes: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

enum class InstallmentStatus {
    ACTIVE,
    COMPLETED,
    OVERDUE,
    CANCELLED
}