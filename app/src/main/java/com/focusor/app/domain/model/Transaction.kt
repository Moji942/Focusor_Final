package com.focusor.app.domain.model

import java.math.BigDecimal
import java.time.LocalDateTime

data class Transaction(
    val id: Long = 0,
    val cardId: Long? = null, // null for cash transactions
    val amount: BigDecimal,
    val description: String,
    val category: String,
    val transactionType: TransactionType,
    val transactionDate: LocalDateTime = LocalDateTime.now(),
    val isRecurring: Boolean = false,
    val recurringInterval: String? = null, // "monthly", "weekly", etc.
    val notes: String = ""
) {
    
    val isIncome: Boolean
        get() = transactionType == TransactionType.INCOME
    
    val isExpense: Boolean
        get() = transactionType == TransactionType.EXPENSE
    
    val isTransfer: Boolean
        get() = transactionType == TransactionType.TRANSFER
    
    val absoluteAmount: BigDecimal
        get() = amount.abs()
    
    fun isValid(): Boolean {
        return amount != BigDecimal.ZERO && 
               description.isNotBlank() && 
               category.isNotBlank()
    }
}

enum class TransactionType {
    INCOME, EXPENSE, TRANSFER
}