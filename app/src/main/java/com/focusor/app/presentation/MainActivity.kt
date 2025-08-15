package com.focusor.app.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.focusor.app.presentation.navigation.BottomNavItem
import com.focusor.app.presentation.navigation.DrawerNavItem
import com.focusor.app.presentation.screens.ai.AiAssistantScreen
import com.focusor.app.presentation.screens.dashboard.DashboardScreen
import com.focusor.app.presentation.screens.education.EducationScreen
import com.focusor.app.presentation.screens.finance.FinanceScreen
import com.focusor.app.presentation.screens.notes.NotesScreen
import com.focusor.app.presentation.screens.settings.SettingsScreen
import com.focusor.app.presentation.theme.FocusorTheme
import com.focusor.app.presentation.ui.components.BottomNavigationBar
import com.focusor.app.presentation.ui.components.DrawerContent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            FocusorTheme {
                FocusorApp()
            }
        }
    }
}

@Composable
fun FocusorApp() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedDrawerItem by remember { mutableStateOf(DrawerNavItem.Dashboard) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomNavItems = listOf(
        BottomNavItem.Education,
        BottomNavItem.Finance,
        BottomNavItem.AiAssistant,
        BottomNavItem.Notes,
        BottomNavItem.Settings
    )

    val drawerItems = listOf(
        DrawerNavItem.Dashboard,
        DrawerNavItem.History,
        DrawerNavItem.Backup,
        DrawerNavItem.Help
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                DrawerContent(
                    items = drawerItems,
                    selectedItem = selectedDrawerItem,
                    onItemClick = { item ->
                        selectedDrawerItem = item
                        scope.launch {
                            drawerState.close()
                        }
                        when (item) {
                            DrawerNavItem.Dashboard -> {
                                navController.navigate("dashboard") {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                            DrawerNavItem.History -> {
                                navController.navigate("history") {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                            DrawerNavItem.Backup -> {
                                navController.navigate("backup") {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                            DrawerNavItem.Help -> {
                                navController.navigate("help") {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    }
                )
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                BottomNavigationBar(
                    items = bottomNavItems,
                    currentDestination = currentDestination,
                    onItemClick = { item ->
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = BottomNavItem.Education.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(BottomNavItem.Education.route) {
                    EducationScreen()
                }
                composable(BottomNavItem.Finance.route) {
                    FinanceScreen()
                }
                composable(BottomNavItem.AiAssistant.route) {
                    AiAssistantScreen()
                }
                composable(BottomNavItem.Notes.route) {
                    NotesScreen()
                }
                composable(BottomNavItem.Settings.route) {
                    SettingsScreen()
                }
                composable("dashboard") {
                    DashboardScreen()
                }
                composable("history") {
                    // TODO: Implement History Screen
                }
                composable("backup") {
                    // TODO: Implement Backup Screen
                }
                composable("help") {
                    // TODO: Implement Help Screen
                }
            }
        }
    }
}