package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.example.ui.LinguaApp
import com.example.ui.LinguaViewModelFactory
import com.example.ui.navigation.Screen
import com.example.ui.screens.MainViewModel
import com.example.ui.theme.LinguaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        val app = application as LinguaApplication
        val factory = LinguaViewModelFactory(app.repository, app.userPreferencesRepository)
        val mainViewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        setContent {
            val userPrefs by mainViewModel.userPrefs.collectAsState()
            
            val isDark = userPrefs?.isDarkMode ?: false
            
            LinguaTheme(darkTheme = isDark, dynamicColor = false) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    if (userPrefs != null) {
                        val startDestination = if (userPrefs!!.hasCompletedOnboarding) {
                            Screen.Home.route
                        } else {
                            Screen.Onboarding.route
                        }
                        LinguaApp(factory, startDestination)
                    }
                }
            }
        }
    }
}
