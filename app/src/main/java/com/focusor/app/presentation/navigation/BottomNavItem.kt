package com.focusor.app.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Note
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

enum class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    Education(
        route = "education",
        title = "Education",
        icon = Icons.Default.Book
    ),
    Finance(
        route = "finance",
        title = "Finance",
        icon = Icons.Default.AccountBalance
    ),
    AiAssistant(
        route = "ai_assistant",
        title = "AI Assistant",
        icon = Icons.Default.Chat
    ),
    Notes(
        route = "notes",
        title = "Notes",
        icon = Icons.Default.Note
    ),
    Settings(
        route = "settings",
        title = "Settings",
        icon = Icons.Default.Settings
    )
}