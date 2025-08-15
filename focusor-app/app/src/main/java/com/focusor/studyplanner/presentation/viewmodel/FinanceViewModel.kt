package com.focusor.studyplanner.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.focusor.studyplanner.data.entity.*
import com.focusor.studyplanner.domain.repository.FinanceRepository
import com.focusor.studyplanner.domain.usecase.finance.FinanceCalculationEngine
import com.focusor.studyplanner.presentation.ui.finance.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class FinanceViewModel @Inject constructor(
    private val repository: FinanceRepository,
    private val calculationEngine: FinanceCalculationEngine
) : ViewModel() {
    
    private val _cards = MutableStateFlow<List<CardData>>(emptyList())
    val cards: StateFlow<List<CardData>> = _cards.asStateFlow()
    
    private val _recentTransactions = MutableStateFlow<List<TransactionData>>(emptyList())
    val recentTransactions: StateFlow<List<TransactionData>> = _recentTransactions.asStateFlow()
    
    private val _activeInstallments = MutableStateFlow<List<InstallmentData>>(emptyList())
    val activeInstallments: StateFlow<List<InstallmentData>> = _activeInstallments.asStateFlow()
    
    private val _financialHealth = MutableStateFlow(FinancialHealthData())
    val financialHealth: StateFlow<FinancialHealthData> = _financialHealth.asStateFlow()
    
    private val _insights = MutableStateFlow<List<String>>(emptyList())
    val insights: StateFlow<List<String>> = _insights.asStateFlow()
    
    init {
        loadCards()
        loadTransactions()
        loadInstallments()
        calculateFinancialHealth()
    }
    
    private fun loadCards() {
        viewModelScope.launch {
            repository.getAllActiveCards().collect { cardEntities ->
                _cards.value = cardEntities.map { entity ->
                    CardData(
                        id = entity.id,
                        bankName = entity.bankName,
                        lastFourDigits = entity.cardNumberEncrypted.takeLast(4),
                        currentBalance = entity.currentBalance,
                        colorCode = entity.colorCode
                    )
                }
                calculateFinancialHealth()
            }
        }
    }
    
    private fun loadTransactions() {
        viewModelScope.launch {
            repository.getAllTransactions().collect { transactionEntities ->
                _recentTransactions.value = transactionEntities.take(20).map { entity ->
                    TransactionData(
                        id = entity.id,
                        description = entity.description,
                        amount = entity.amount,
                        type = entity.type.name,
                        category = entity.category,
                        date = entity.date.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
                    )
                }
            }
        }
    }
    
    private fun loadInstallments() {
        viewModelScope.launch {
            repository.getActiveInstallments().collect { installmentEntities ->
                _activeInstallments.value = installmentEntities.map { entity ->
                    val nextPaymentDate = entity.startDate.plusMonths(entity.paidCount.toLong())
                    InstallmentData(
                        id = entity.id,
                        title = entity.title,
                        monthlyPayment = entity.monthlyPayment,
                        paidCount = entity.paidCount,
                        totalCount = entity.installmentCount,
                        nextPaymentDate = nextPaymentDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")),
                        status = entity.status.name
                    )
                }
                calculateFinancialHealth()
            }
        }
    }
    
    private fun calculateFinancialHealth() {
        viewModelScope.launch {
            val totalBalance = repository.getTotalBalance()
            val startOfMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0)
            val endOfMonth = startOfMonth.plusMonths(1).minusSeconds(1)
            
            val monthlyIncome = repository.getMonthlyIncome(startOfMonth, endOfMonth)
            val monthlyExpenses = repository.getMonthlyExpenses(startOfMonth, endOfMonth)
            
            // Calculate financial health score
            val emergencyFundMonths = calculationEngine.calculateEmergencyFundMonths(
                totalBalance,
                monthlyExpenses
            )
            
            val debtToIncomeRatio = if (monthlyIncome > BigDecimal.ZERO) {
                monthlyExpenses.divide(monthlyIncome, 2, java.math.RoundingMode.HALF_UP).toDouble()
            } else 0.0
            
            val savingsRate = if (monthlyIncome > BigDecimal.ZERO) {
                (monthlyIncome.subtract(monthlyExpenses))
                    .divide(monthlyIncome, 2, java.math.RoundingMode.HALF_UP)
                    .toDouble()
            } else 0.0
            
            val score = calculationEngine.calculateFinancialHealthScore(
                emergencyFundMonths,
                debtToIncomeRatio,
                savingsRate,
                1.0 // Assuming perfect payment history for now
            )
            
            _financialHealth.value = FinancialHealthData(
                score = score,
                totalBalance = totalBalance,
                monthlyIncome = monthlyIncome,
                monthlyExpenses = monthlyExpenses
            )
            
            // Generate insights
            generateFinancialInsights()
        }
    }
    
    private suspend fun generateFinancialInsights() {
        val cardsList = repository.getAllActiveCards().first()
        val installmentsList = repository.getActiveInstallments().first()
        
        val startOfMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0)
        val endOfMonth = startOfMonth.plusMonths(1).minusSeconds(1)
        
        val monthlyIncome = repository.getMonthlyIncome(startOfMonth, endOfMonth)
        val monthlyExpenses = repository.getMonthlyExpenses(startOfMonth, endOfMonth)
        val totalBalance = repository.getTotalBalance()
        
        val insights = calculationEngine.generateFinancialInsights(
            cardsList,
            installmentsList,
            monthlyIncome,
            monthlyExpenses,
            totalBalance
        )
        
        _insights.value = insights
    }
    
    suspend fun addCard(input: CardInput) {
        val entity = CardEntity(
            bankName = input.bankName,
            cardNumberEncrypted = input.cardNumber.takeLast(4), // Store only last 4 digits
            currentBalance = input.balance,
            currency = "IRR",
            colorCode = generateCardColor(),
            isActive = true,
            cardType = "DEBIT"
        )
        
        repository.insertCard(entity)
    }
    
    suspend fun addTransaction(input: TransactionInput) {
        val entity = TransactionEntity(
            cardId = input.cardId,
            amount = input.amount,
            type = TransactionType.valueOf(input.type),
            category = input.category,
            description = input.description,
            date = LocalDateTime.now()
        )
        
        repository.insertTransaction(entity)
    }
    
    suspend fun addInstallment(input: InstallmentInput) {
        val monthlyPayment = calculationEngine.calculateMonthlyInstallment(
            input.totalAmount,
            0.0, // No interest for now
            input.installmentCount
        )
        
        val entity = InstallmentEntity(
            title = input.title,
            totalAmount = input.totalAmount,
            installmentCount = input.installmentCount,
            monthlyPayment = monthlyPayment,
            sourceCardId = input.cardId,
            startDate = LocalDate.now(),
            status = InstallmentStatus.ACTIVE,
            category = "General"
        )
        
        repository.insertInstallment(entity)
    }
    
    suspend fun makeInstallmentPayment(installmentId: Long) {
        repository.incrementInstallmentPayment(installmentId)
    }
    
    private fun generateCardColor(): String {
        val colors = listOf(
            "#1976D2", "#388E3C", "#D32F2F", "#7B1FA2",
            "#F57C00", "#0288D1", "#689F38", "#C2185B"
        )
        return colors.random()
    }
}