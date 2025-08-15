package com.focusor.app.presentation.screens.education.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.focusor.app.domain.model.Subject
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSubjectDialog(
    onDismiss: () -> Unit,
    onSubjectAdded: (Subject) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var source by remember { mutableStateOf("") }
    var totalPages by remember { mutableStateOf("") }
    var targetDate by remember { mutableStateOf(LocalDate.now().plusDays(30)) }
    var priority by remember { mutableStateOf(1) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Subject") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Subject Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                OutlinedTextField(
                    value = source,
                    onValueChange = { source = it },
                    label = { Text("Source") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                OutlinedTextField(
                    value = totalPages,
                    onValueChange = { totalPages = it },
                    label = { Text("Total Pages") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                // Priority slider
                Text("Priority: $priority")
                Slider(
                    value = priority.toFloat(),
                    onValueChange = { priority = it.toInt() },
                    valueRange = 1f..5f,
                    steps = 3
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (name.isNotBlank() && source.isNotBlank() && totalPages.isNotBlank()) {
                        val subject = Subject(
                            name = name,
                            source = source,
                            totalPages = totalPages.toIntOrNull() ?: 0,
                            targetDate = targetDate,
                            priority = priority
                        )
                        onSubjectAdded(subject)
                    }
                }
            ) {
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