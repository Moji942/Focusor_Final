package com.focusor.studyplanner.presentation.ui.finance

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.focusor.studyplanner.presentation.viewmodel.FinanceViewModel
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinanceScreen(
    viewModel: FinanceViewModel = hiltViewModel()
) {
    val cards by viewModel.cards.collectAsState()
    val transactions by viewModel.recentTransactions.collectAsState()
    val installments by viewModel.activeInstallments.collectAsState()
    val financialHealth by viewModel.financialHealth.collectAsState()
    val insights by viewModel.insights.collectAsState()
    
    var showAddCardDialog by remember { mutableStateOf(false) }
    var showAddTransactionDialog by remember { mutableStateOf(false) }
    var showAddInstallmentDialog by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(0) }
    
    val scope = rememberCoroutineScope()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Financial Health Score Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = when {
                    financialHealth.score >= 75 -> MaterialTheme.colorScheme.primaryContainer
                    financialHealth.score >= 50 -> MaterialTheme.colorScheme.secondaryContainer
                    else -> MaterialTheme.colorScheme.errorContainer
                }
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "Financial Health",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            when {
                                financialHealth.score >= 75 -> "Excellent"
                                financialHealth.score >= 50 -> "Good"
                                financialHealth.score >= 25 -> "Fair"
                                else -> "Needs Attention"
                            },
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(40.dp))
                            .background(MaterialTheme.colorScheme.surface)
                    ) {
                        Text(
                            "${financialHealth.score}",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = when {
                                financialHealth.score >= 75 -> MaterialTheme.colorScheme.primary
                                financialHealth.score >= 50 -> MaterialTheme.colorScheme.secondary
                                else -> MaterialTheme.colorScheme.error
                            }
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Financial Metrics
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    MetricItem(
                        label = "Total Balance",
                        value = formatCurrency(financialHealth.totalBalance),
                        icon = Icons.Default.AccountBalance
                    )
                    MetricItem(
                        label = "Monthly Income",
                        value = formatCurrency(financialHealth.monthlyIncome),
                        icon = Icons.Default.TrendingUp
                    )
                    MetricItem(
                        label = "Monthly Expenses",
                        value = formatCurrency(financialHealth.monthlyExpenses),
                        icon = Icons.Default.TrendingDown
                    )
                }
            }
        }
        
        // Financial Insights
        AnimatedVisibility(visible = insights.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Lightbulb,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Financial Insights",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    insights.forEach { insight ->
                        Row(modifier = Modifier.padding(vertical = 4.dp)) {
                            Text("• ", style = MaterialTheme.typography.bodyMedium)
                            Text(insight, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
        
        // Cards Section
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Your Cards",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            
            IconButton(onClick = { showAddCardDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Card")
            }
        }
        
        // Cards Carousel
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(cards) { card ->
                BankCard(
                    card = card,
                    onClick = { /* Navigate to card details */ }
                )
            }
            
            if (cards.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .width(320.dp)
                            .height(180.dp)
                            .clickable { showAddCardDialog = true },
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    Icons.Default.Add,
                                    contentDescription = null,
                                    modifier = Modifier.size(48.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    "Add Your First Card",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Tabs for Transactions and Installments
        TabRow(
            selectedTabIndex = selectedTab,
            modifier = Modifier.fillMaxWidth()
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Transactions") }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Installments") }
            )
        }
        
        // Content based on selected tab
        when (selectedTab) {
            0 -> {
                // Transactions List
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "Recent Transactions",
                                style = MaterialTheme.typography.titleMedium
                            )
                            TextButton(onClick = { showAddTransactionDialog = true }) {
                                Icon(Icons.Default.Add, contentDescription = null)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Add")
                            }
                        }
                    }
                    
                    items(transactions) { transaction ->
                        TransactionItem(transaction = transaction)
                    }
                    
                    if (transactions.isEmpty()) {
                        item {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 32.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(32.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        Icons.Default.Receipt,
                                        contentDescription = null,
                                        modifier = Modifier.size(64.dp),
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        "No transactions yet",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Text(
                                        "Add your first transaction to start tracking",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
            
            1 -> {
                // Installments List
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "Active Installments",
                                style = MaterialTheme.typography.titleMedium
                            )
                            TextButton(onClick = { showAddInstallmentDialog = true }) {
                                Icon(Icons.Default.Add, contentDescription = null)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Add")
                            }
                        }
                    }
                    
                    items(installments) { installment ->
                        InstallmentItem(
                            installment = installment,
                            onPayment = {
                                scope.launch {
                                    viewModel.makeInstallmentPayment(installment.id)
                                }
                            }
                        )
                    }
                    
                    if (installments.isEmpty()) {
                        item {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 32.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(32.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        Icons.Default.CalendarMonth,
                                        contentDescription = null,
                                        modifier = Modifier.size(64.dp),
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        "No installments",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Text(
                                        "Track your installment payments here",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    // Dialogs
    if (showAddCardDialog) {
        AddCardDialog(
            onDismiss = { showAddCardDialog = false },
            onConfirm = { card ->
                scope.launch {
                    viewModel.addCard(card)
                    showAddCardDialog = false
                }
            }
        )
    }
    
    if (showAddTransactionDialog) {
        AddTransactionDialog(
            cards = cards,
            onDismiss = { showAddTransactionDialog = false },
            onConfirm = { transaction ->
                scope.launch {
                    viewModel.addTransaction(transaction)
                    showAddTransactionDialog = false
                }
            }
        )
    }
    
    if (showAddInstallmentDialog) {
        AddInstallmentDialog(
            cards = cards,
            onDismiss = { showAddInstallmentDialog = false },
            onConfirm = { installment ->
                scope.launch {
                    viewModel.addInstallment(installment)
                    showAddInstallmentDialog = false
                }
            }
        )
    }
}

@Composable
fun BankCard(
    card: CardData,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(320.dp)
            .height(180.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(android.graphics.Color.parseColor(card.colorCode)),
                            Color(android.graphics.Color.parseColor(card.colorCode)).copy(alpha = 0.7f)
                        )
                    )
                )
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        card.bankName,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        Icons.Default.CreditCard,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
                
                Text(
                    "**** **** **** ${card.lastFourDigits}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White.copy(alpha = 0.9f)
                )
                
                Column {
                    Text(
                        "Balance",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                    Text(
                        formatCurrency(card.currentBalance),
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun TransactionItem(transaction: TransactionData) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    when (transaction.type) {
                        "INCOME" -> Icons.Default.ArrowDownward
                        "EXPENSE" -> Icons.Default.ArrowUpward
                        else -> Icons.Default.SwapHoriz
                    },
                    contentDescription = null,
                    tint = when (transaction.type) {
                        "INCOME" -> MaterialTheme.colorScheme.primary
                        "EXPENSE" -> MaterialTheme.colorScheme.error
                        else -> MaterialTheme.colorScheme.secondary
                    },
                    modifier = Modifier.size(32.dp)
                )
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Column {
                    Text(
                        transaction.description,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        "${transaction.category} • ${transaction.date}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            Text(
                formatCurrency(transaction.amount),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = when (transaction.type) {
                    "INCOME" -> MaterialTheme.colorScheme.primary
                    "EXPENSE" -> MaterialTheme.colorScheme.error
                    else -> MaterialTheme.colorScheme.onSurface
                }
            )
        }
    }
}

@Composable
fun InstallmentItem(
    installment: InstallmentData,
    onPayment: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        installment.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "${installment.paidCount}/${installment.totalCount} payments",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                
                Text(
                    formatCurrency(installment.monthlyPayment),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            LinearProgressIndicator(
                progress = installment.paidCount.toFloat() / installment.totalCount,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Next payment: ${installment.nextPaymentDate}",
                    style = MaterialTheme.typography.bodySmall
                )
                
                Button(
                    onClick = onPayment,
                    enabled = installment.status == "ACTIVE"
                ) {
                    Text("Pay Now")
                }
            }
        }
    }
}

@Composable
fun MetricItem(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            value,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Text(
            label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

// Data classes
data class CardData(
    val id: Long,
    val bankName: String,
    val lastFourDigits: String,
    val currentBalance: BigDecimal,
    val colorCode: String
)

data class TransactionData(
    val id: Long,
    val description: String,
    val amount: BigDecimal,
    val type: String,
    val category: String,
    val date: String
)

data class InstallmentData(
    val id: Long,
    val title: String,
    val monthlyPayment: BigDecimal,
    val paidCount: Int,
    val totalCount: Int,
    val nextPaymentDate: String,
    val status: String
)

data class FinancialHealthData(
    val score: Int = 0,
    val totalBalance: BigDecimal = BigDecimal.ZERO,
    val monthlyIncome: BigDecimal = BigDecimal.ZERO,
    val monthlyExpenses: BigDecimal = BigDecimal.ZERO
)

// Helper function
fun formatCurrency(amount: BigDecimal): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("fa", "IR"))
    val toman = amount.divide(BigDecimal(10))
    return "${formatter.format(toman).replace("IRR", "")} تومان"
}

// Dialog implementations
@Composable
fun AddCardDialog(
    onDismiss: () -> Unit,
    onConfirm: (CardInput) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Card") },
        text = {
            Column {
                Text("Card details form will be implemented here")
            }
        },
        confirmButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun AddTransactionDialog(
    cards: List<CardData>,
    onDismiss: () -> Unit,
    onConfirm: (TransactionInput) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Transaction") },
        text = {
            Column {
                Text("Transaction form will be implemented here")
            }
        },
        confirmButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun AddInstallmentDialog(
    cards: List<CardData>,
    onDismiss: () -> Unit,
    onConfirm: (InstallmentInput) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Installment") },
        text = {
            Column {
                Text("Installment form will be implemented here")
            }
        },
        confirmButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

data class CardInput(
    val bankName: String,
    val cardNumber: String,
    val balance: BigDecimal
)

data class TransactionInput(
    val cardId: Long,
    val amount: BigDecimal,
    val type: String,
    val description: String,
    val category: String
)

data class InstallmentInput(
    val title: String,
    val totalAmount: BigDecimal,
    val installmentCount: Int,
    val cardId: Long
)