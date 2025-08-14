package com.focusor.studyplanner.presentation.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.focusor.studyplanner.R
import com.focusor.studyplanner.presentation.ui.dashboard.DashboardScreen
import com.focusor.studyplanner.presentation.ui.education.EducationScreen
import com.focusor.studyplanner.presentation.ui.finance.FinanceScreen
import com.focusor.studyplanner.presentation.ui.ai.AIAssistantScreen
import com.focusor.studyplanner.presentation.ui.notes.NotesScreen
import com.focusor.studyplanner.presentation.ui.settings.SettingsScreen
import kotlinx.coroutines.launch
import androidx.compose.ui.unit.dp

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Dashboard : Screen("dashboard", "Dashboard", Icons.Default.Dashboard)
    object Education : Screen("education", "Education", Icons.Default.School)
    object Finance : Screen("finance", "Finance", Icons.Default.AccountBalance)
    object AI : Screen("ai", "AI Assistant", Icons.Default.SmartToy)
    object Notes : Screen("notes", "Notes", Icons.Default.Note)
    object Settings : Screen("settings", "Settings", Icons.Default.Settings)
}

val bottomNavItems = listOf(
    Screen.Education,
    Screen.Finance,
    Screen.AI,
    Screen.Notes,
    Screen.Settings
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FocusorNavHost(navController: NavHostController) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.height(WindowInsets.statusBars.asPaddingValues().calculateTopPadding()))
                
                NavigationDrawerItem(
                    icon = { Icon(Screen.Dashboard.icon, contentDescription = null) },
                    label = { Text(Screen.Dashboard.title) },
                    selected = false,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                            navController.navigate(Screen.Dashboard.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                )
                
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                
                Text(
                    "Analytics",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
                
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Analytics, contentDescription = null) },
                    label = { Text("History & Analytics") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        // Navigate to analytics
                    }
                )
                
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.CloudUpload, contentDescription = null) },
                    label = { Text("Backup & Restore") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        // Navigate to backup
                    }
                )
                
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Help, contentDescription = null) },
                    label = { Text("Help & About") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        // Navigate to help
                    }
                )
            }
        }
    ) {
        Scaffold(
            bottomBar = {
                NavigationBar {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination
                    
                    bottomNavItems.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = null) },
                            label = { Text(screen.title) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            },
            topBar = {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                val title = bottomNavItems.find { it.route == currentRoute }?.title ?: "Focusor"
                
                TopAppBar(
                    title = { Text(title) },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            }
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = Screen.Education.route,
                modifier = Modifier.padding(paddingValues)
            ) {
                composable(Screen.Dashboard.route) { DashboardScreen() }
                composable(Screen.Education.route) { EducationScreen() }
                composable(Screen.Finance.route) { FinanceScreen() }
                composable(Screen.AI.route) { AIAssistantScreen() }
                composable(Screen.Notes.route) { NotesScreen() }
                composable(Screen.Settings.route) { SettingsScreen() }
            }
        }
    }
}