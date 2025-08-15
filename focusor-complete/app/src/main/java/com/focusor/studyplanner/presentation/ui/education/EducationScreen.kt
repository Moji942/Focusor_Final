package com.focusor.studyplanner.presentation.ui.education

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.focusor.studyplanner.presentation.viewmodel.EducationViewModel
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EducationScreen(
    viewModel: EducationViewModel = hiltViewModel()
) {
    val subjects by viewModel.subjects.collectAsState()
    val todayProgress by viewModel.todayProgress.collectAsState()
    val recommendations by viewModel.recommendations.collectAsState()
    val conflicts by viewModel.conflicts.collectAsState()
    
    var showAddSubjectDialog by remember { mutableStateOf(false) }
    var showStudySessionDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Today's Overview Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    "Today's Progress",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "${todayProgress.pagesCompleted}",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text("Pages Read", style = MaterialTheme.typography.bodySmall)
                    }
                    
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "${todayProgress.pagesTarget}",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Text("Target Pages", style = MaterialTheme.typography.bodySmall)
                    }
                    
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "${String.format("%.1f", todayProgress.hoursStudied)}h",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                        Text("Hours Studied", style = MaterialTheme.typography.bodySmall)
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                LinearProgressIndicator(
                    progress = (todayProgress.pagesCompleted.toFloat() / todayProgress.pagesTarget.coerceAtLeast(1)),
                    modifier = Modifier.fillMaxWidth()
                )
                
                Text(
                    "${(todayProgress.pagesCompleted * 100 / todayProgress.pagesTarget.coerceAtLeast(1))}% Complete",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
        
        // Smart Recommendations
        AnimatedVisibility(visible = recommendations.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Lightbulb,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Smart Recommendations",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    recommendations.forEach { recommendation ->
                        Row(modifier = Modifier.padding(vertical = 4.dp)) {
                            Text("• ", style = MaterialTheme.typography.bodyMedium)
                            Text(recommendation, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
        
        // Conflicts Warning
        AnimatedVisibility(visible = conflicts.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Warning,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Schedule Conflicts",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    conflicts.forEach { conflict ->
                        Row(modifier = Modifier.padding(vertical = 4.dp)) {
                            Text("⚠ ", style = MaterialTheme.typography.bodyMedium)
                            Text(conflict, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
        
        // Subjects List
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Active Subjects",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            
            IconButton(onClick = { showAddSubjectDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Subject")
            }
        }
        
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(subjects) { subject ->
                SubjectCard(
                    subject = subject,
                    onStartSession = {
                        viewModel.selectSubject(subject)
                        showStudySessionDialog = true
                    },
                    onUpdateProgress = { pages ->
                        scope.launch {
                            viewModel.updateProgress(subject.id, pages)
                        }
                    }
                )
            }
            
            if (subjects.isEmpty()) {
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
                                Icons.Default.School,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "No subjects yet",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                "Add your first subject to start tracking",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = { showAddSubjectDialog = true }) {
                                Icon(Icons.Default.Add, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Add Subject")
                            }
                        }
                    }
                }
            }
        }
    }
    
    // Add Subject Dialog
    if (showAddSubjectDialog) {
        AddSubjectDialog(
            onDismiss = { showAddSubjectDialog = false },
            onConfirm = { subject ->
                scope.launch {
                    viewModel.addSubject(subject)
                    showAddSubjectDialog = false
                }
            }
        )
    }
    
    // Study Session Dialog
    if (showStudySessionDialog) {
        StudySessionDialog(
            onDismiss = { showStudySessionDialog = false },
            onConfirm = { session ->
                scope.launch {
                    viewModel.addStudySession(session)
                    showStudySessionDialog = false
                }
            }
        )
    }
}

@Composable
fun SubjectCard(
    subject: SubjectData,
    onStartSession: () -> Unit,
    onUpdateProgress: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        subject.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        subject.resourceType,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Chip(
                    onClick = { },
                    colors = ChipDefaults.chipColors(
                        containerColor = when (subject.priority) {
                            5 -> MaterialTheme.colorScheme.error
                            4 -> MaterialTheme.colorScheme.secondary
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        }
                    )
                ) {
                    Text("P${subject.priority}")
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Progress Bar
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "${subject.completedPages} / ${subject.totalPages} pages",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        "${(subject.completedPages * 100 / subject.totalPages)}%",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                LinearProgressIndicator(
                    progress = subject.completedPages.toFloat() / subject.totalPages,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Stats Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "${subject.dailyPagesRequired}",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text("pages/day", style = MaterialTheme.typography.labelSmall)
                }
                
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "${String.format("%.1f", subject.currentReadingSpeed)}",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text("pages/hour", style = MaterialTheme.typography.labelSmall)
                }
                
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "${subject.daysRemaining}",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (subject.daysRemaining < 7) 
                            MaterialTheme.colorScheme.error 
                        else 
                            MaterialTheme.colorScheme.onSurface
                    )
                    Text("days left", style = MaterialTheme.typography.labelSmall)
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onStartSession) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Start Session")
                }
                
                TextButton(onClick = { /* Quick update */ }) {
                    Icon(Icons.Default.Edit, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Quick Update")
                }
            }
        }
    }
}

// Data classes for UI
data class SubjectData(
    val id: Long,
    val name: String,
    val resourceType: String,
    val totalPages: Int,
    val completedPages: Int,
    val priority: Int,
    val dailyPagesRequired: Int,
    val currentReadingSpeed: Double,
    val daysRemaining: Int,
    val isOnTrack: Boolean
)

data class TodayProgress(
    val pagesCompleted: Int = 0,
    val pagesTarget: Int = 1,
    val hoursStudied: Double = 0.0
)

@Composable
fun AddSubjectDialog(
    onDismiss: () -> Unit,
    onConfirm: (SubjectInput) -> Unit
) {
    // Implementation for add subject dialog
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Subject") },
        text = {
            Column {
                Text("Subject details form will be implemented here")
                // Add form fields
            }
        },
        confirmButton = {
            TextButton(onClick = { 
                // Create subject input and confirm
                onDismiss()
            }) {
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
fun StudySessionDialog(
    onDismiss: () -> Unit,
    onConfirm: (SessionInput) -> Unit
) {
    // Implementation for study session dialog
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Log Study Session") },
        text = {
            Column {
                Text("Session details form will be implemented here")
                // Add form fields
            }
        },
        confirmButton = {
            TextButton(onClick = { 
                // Create session input and confirm
                onDismiss()
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

data class SubjectInput(
    val name: String,
    val resourceType: String,
    val totalPages: Int,
    val targetDate: String,
    val priority: Int
)

data class SessionInput(
    val subjectId: Long,
    val pagesRead: Int,
    val hoursSpent: Double,
    val quality: Int,
    val notes: String
)