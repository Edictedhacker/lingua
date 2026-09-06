package com.example.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Calendar

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class UserPreferencesRepository(private val dataStore: DataStore<Preferences>) {
    
    private object Keys {
        val HAS_COMPLETED_ONBOARDING = booleanPreferencesKey("has_completed_onboarding")
        val CURRENT_LANGUAGE_CODE = stringPreferencesKey("current_language_code")
        val DAILY_GOAL_WORDS = intPreferencesKey("daily_goal_words")
        val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
        
        // Streak Tracking
        val CURRENT_STREAK = intPreferencesKey("current_streak")
        val LONGEST_STREAK = intPreferencesKey("longest_streak")
        val LAST_LEARNING_DATE = longPreferencesKey("last_learning_date")
        
        // Overall Progress
        val XP = intPreferencesKey("xp")
    }
    
    val userPreferencesFlow: Flow<UserPreferences> = dataStore.data.map { prefs ->
        UserPreferences(
            hasCompletedOnboarding = prefs[Keys.HAS_COMPLETED_ONBOARDING] ?: false,
            currentLanguageCode = prefs[Keys.CURRENT_LANGUAGE_CODE] ?: "es",
            dailyGoalWords = prefs[Keys.DAILY_GOAL_WORDS] ?: 10,
            isDarkMode = prefs[Keys.IS_DARK_MODE] ?: false,
            currentStreak = prefs[Keys.CURRENT_STREAK] ?: 0,
            longestStreak = prefs[Keys.LONGEST_STREAK] ?: 0,
            lastLearningDate = prefs[Keys.LAST_LEARNING_DATE] ?: 0L,
            xp = prefs[Keys.XP] ?: 0
        )
    }
    
    suspend fun updateOnboardingStatus(completed: Boolean) {
        dataStore.edit { it[Keys.HAS_COMPLETED_ONBOARDING] = completed }
    }
    
    suspend fun updateCurrentLanguage(code: String) {
        dataStore.edit { it[Keys.CURRENT_LANGUAGE_CODE] = code }
    }
    
    suspend fun updateDailyGoal(words: Int) {
        dataStore.edit { it[Keys.DAILY_GOAL_WORDS] = words }
    }
    
    suspend fun updateDarkMode(isDark: Boolean) {
        dataStore.edit { it[Keys.IS_DARK_MODE] = isDark }
    }
    
    suspend fun addXp(amount: Int) {
        dataStore.edit { prefs ->
            val current = prefs[Keys.XP] ?: 0
            prefs[Keys.XP] = current + amount
        }
    }
    
    suspend fun resetProgress() {
        dataStore.edit { prefs ->
            prefs[Keys.CURRENT_STREAK] = 0
            prefs[Keys.LONGEST_STREAK] = 0
            prefs[Keys.LAST_LEARNING_DATE] = 0L
            prefs[Keys.XP] = 0
        }
    }
    
    suspend fun recordLearningSession() {
        dataStore.edit { prefs ->
            val currentStreak = prefs[Keys.CURRENT_STREAK] ?: 0
            val longestStreak = prefs[Keys.LONGEST_STREAK] ?: 0
            val lastDate = prefs[Keys.LAST_LEARNING_DATE] ?: 0L
            
            val today = getStartOfDay(System.currentTimeMillis())
            val last = getStartOfDay(lastDate)
            
            val daysDifference = ((today - last) / (1000 * 60 * 60 * 24)).toInt()
            
            if (daysDifference == 1) {
                // Consecutive day
                val newStreak = currentStreak + 1
                prefs[Keys.CURRENT_STREAK] = newStreak
                if (newStreak > longestStreak) {
                    prefs[Keys.LONGEST_STREAK] = newStreak
                }
            } else if (daysDifference > 1) {
                // Streak broken
                prefs[Keys.CURRENT_STREAK] = 1
                if (longestStreak == 0) {
                    prefs[Keys.LONGEST_STREAK] = 1
                }
            } else if (daysDifference == 0 && currentStreak == 0) {
                // First session today, streak was 0
                prefs[Keys.CURRENT_STREAK] = 1
                if (longestStreak == 0) {
                    prefs[Keys.LONGEST_STREAK] = 1
                }
            }
            
            prefs[Keys.LAST_LEARNING_DATE] = System.currentTimeMillis()
        }
    }
    
    private fun getStartOfDay(timeInMillis: Long): Long {
        if (timeInMillis == 0L) return 0L
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeInMillis
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
}

data class UserPreferences(
    val hasCompletedOnboarding: Boolean,
    val currentLanguageCode: String,
    val dailyGoalWords: Int,
    val isDarkMode: Boolean,
    val currentStreak: Int,
    val longestStreak: Int,
    val lastLearningDate: Long,
    val xp: Int
)
