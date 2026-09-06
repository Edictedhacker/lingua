package com.example.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.LanguageEntity
import com.example.data.LinguaRepository
import com.example.data.UserPreferences
import com.example.data.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: LinguaRepository,
    private val userPrefsRepository: UserPreferencesRepository
) : ViewModel() {
    
    val userPrefs = userPrefsRepository.userPreferencesFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )
    
    private val _currentLanguage = MutableStateFlow<LanguageEntity?>(null)
    val currentLanguage: StateFlow<LanguageEntity?> = _currentLanguage.asStateFlow()
    
    init {
        viewModelScope.launch {
            userPrefsRepository.userPreferencesFlow.collect { prefs ->
                val lang = repository.getLanguage(prefs.currentLanguageCode)
                _currentLanguage.value = lang
            }
        }
    }
    
    fun completeOnboarding() {
        viewModelScope.launch {
            userPrefsRepository.updateOnboardingStatus(true)
        }
    }
    
    fun setLanguage(code: String) {
        viewModelScope.launch {
            userPrefsRepository.updateCurrentLanguage(code)
        }
    }
    
    fun setDailyGoal(words: Int) {
        viewModelScope.launch {
            userPrefsRepository.updateDailyGoal(words)
        }
    }
    
    fun toggleDarkMode(isDark: Boolean) {
        viewModelScope.launch {
            userPrefsRepository.updateDarkMode(isDark)
        }
    }
    
    fun resetProgress() {
        viewModelScope.launch {
            userPrefsRepository.resetProgress()
        }
    }
}
