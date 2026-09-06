package com.example.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.LinguaViewModelFactory
import com.example.ui.screens.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModelFactory: LinguaViewModelFactory) {
    val viewModel: MainViewModel = viewModel(factory = viewModelFactory)
    val userPrefs by viewModel.userPrefs.collectAsState()
    
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Settings", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                SettingsSection(title = "Preferences") {
                    SettingsItem(
                        title = "Dark Mode",
                        subtitle = "Toggle dark theme"
                    ) {
                        Switch(
                            checked = userPrefs?.isDarkMode ?: false,
                            onCheckedChange = { viewModel.toggleDarkMode(it) }
                        )
                    }
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                    SettingsItem(
                        title = "Daily Goal",
                        subtitle = "${userPrefs?.dailyGoalWords ?: 10} words per day"
                    ) {
                        // Normally would open a dialog, for now let's just make it a clickable row
                    }
                }
            }
            
            item {
                SettingsSection(title = "Data") {
                    SettingsItem(
                        title = "Reset Progress",
                        subtitle = "Clear all learning data and streaks",
                        titleColor = MaterialTheme.colorScheme.error
                    ) {
                        Button(
                            onClick = { viewModel.resetProgress() },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                        ) {
                            Text("Reset")
                        }
                    }
                }
            }
            
            item {
                SettingsSection(title = "About") {
                    SettingsItem(title = "Version", subtitle = "1.0.0")
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                    SettingsItem(title = "Privacy Policy")
                }
            }
        }
    }
}

@Composable
fun SettingsSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 8.dp, start = 8.dp)
        )
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
        ) {
            Column(content = content)
        }
    }
}

@Composable
fun SettingsItem(
    title: String,
    subtitle: String? = null,
    titleColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface,
    trailingContent: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = titleColor,
                fontWeight = FontWeight.Medium
            )
            if (subtitle != null) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
        if (trailingContent != null) {
            trailingContent()
        }
    }
}
