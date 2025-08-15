package com.focusor.app.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Help
import androidx.compose.ui.graphics.vector.ImageVector

enum class DrawerNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    Dashboard(
        route = "dashboard",
        title = "Dashboard",
        icon = Icons.Default.Dashboard
    ),
    History(
        route = "history",
        title = "History & Analytics",
        icon = Icons.Default.Analytics
    ),
    Backup(
        route = "backup",
        title = "Backup & Restore",
        icon = Icons.Default.CloudUpload
    ),
    Help(
        route = "help",
        title = "Help & About",
        icon = Icons.Default.Help
    )
}