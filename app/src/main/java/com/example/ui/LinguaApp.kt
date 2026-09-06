package com.example.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ui.navigation.Screen
import com.example.ui.screens.home.HomeScreen
import com.example.ui.screens.learn.LearnScreen
import com.example.ui.screens.phrasebook.PhrasebookScreen
import com.example.ui.screens.progress.ProgressScreen
import com.example.ui.screens.settings.SettingsScreen

@Composable
fun LinguaApp(
    viewModelFactory: LinguaViewModelFactory,
    startDestination: String
) {
    val navController = rememberNavController()

    val bottomNavItems = listOf(
        Screen.Home,
        Screen.Phrasebook,
        Screen.Learn,
        Screen.Progress,
        Screen.Settings
    )

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            
            val showBottomNav = bottomNavItems.any { it.route == currentDestination?.route }
            
            if (showBottomNav) {
                NavigationBar(
                    containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant
                ) {
                    bottomNavItems.forEach { screen ->
                        NavigationBarItem(
                            icon = { 
                                Icon(
                                    imageVector = when (screen) {
                                        Screen.Home -> Icons.Filled.Home
                                        Screen.Phrasebook -> Icons.Filled.MenuBook
                                        Screen.Learn -> Icons.Filled.School
                                        Screen.Progress -> Icons.Filled.Timeline
                                        Screen.Settings -> Icons.Filled.Settings
                                        else -> Icons.Filled.Home
                                    },
                                    contentDescription = screen.route
                                )
                            },
                            label = { 
                                Text(
                                    when (screen) {
                                        Screen.Home -> "Home"
                                        Screen.Phrasebook -> "Phrases"
                                        Screen.Learn -> "Learn"
                                        Screen.Progress -> "Progress"
                                        Screen.Settings -> "Settings"
                                        else -> ""
                                    }
                                )
                            },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            colors = androidx.compose.material3.NavigationBarItemDefaults.colors(
                                indicatorColor = androidx.compose.material3.MaterialTheme.colorScheme.primaryContainer,
                                selectedIconColor = androidx.compose.material3.MaterialTheme.colorScheme.onPrimaryContainer,
                                selectedTextColor = androidx.compose.material3.MaterialTheme.colorScheme.onPrimaryContainer,
                                unselectedIconColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
                                unselectedTextColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Splash.route) {
                // Splash UI
            }
            composable(Screen.Onboarding.route) {
                com.example.ui.screens.onboarding.OnboardingScreen(viewModelFactory = viewModelFactory) {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            }
            composable(Screen.Home.route) {
                HomeScreen(viewModelFactory = viewModelFactory, onContinueClick = {
                    navController.navigate("quiz")
                })
            }
            composable(Screen.Phrasebook.route) {
                PhrasebookScreen(viewModelFactory = viewModelFactory, onNavigateToSearch = {
                    navController.navigate("search")
                })
            }
            composable(Screen.Learn.route) {
                LearnScreen(viewModelFactory = viewModelFactory, onStartFlashcards = {
                    navController.navigate("flashcards")
                }, onNavigateToSearch = {
                    navController.navigate("search")
                })
            }
            composable("flashcards") {
                com.example.ui.screens.learn.FlashcardScreen(
                    viewModelFactory = viewModelFactory,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable("quiz") {
                com.example.ui.screens.quiz.QuizScreen(
                    viewModelFactory = viewModelFactory,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable("search") {
                com.example.ui.screens.search.SearchScreen(
                    viewModelFactory = viewModelFactory,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable(Screen.Progress.route) {
                ProgressScreen(viewModelFactory = viewModelFactory)
            }
            composable(Screen.Settings.route) {
                SettingsScreen(viewModelFactory = viewModelFactory)
            }
        }
    }
}
