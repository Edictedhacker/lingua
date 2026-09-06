package com.example.ui.screens.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.LinguaRepository
import com.example.data.QuizResultEntity
import com.example.data.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProgressViewModel(
    private val repository: LinguaRepository,
    private val userPrefsRepository: UserPreferencesRepository
) : ViewModel() {
    
    val userPrefs = userPrefsRepository.userPreferencesFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )
    
    private val _learnedWordsCount = MutableStateFlow(0)
    val learnedWordsCount: StateFlow<Int> = _learnedWordsCount.asStateFlow()
    
    private val _quizResults = MutableStateFlow<List<QuizResultEntity>>(emptyList())
    val quizResults: StateFlow<List<QuizResultEntity>> = _quizResults.asStateFlow()

    init {
        viewModelScope.launch {
            userPrefsRepository.userPreferencesFlow.collect { prefs ->
                val langCode = prefs.currentLanguageCode
                
                repository.getLearnedWords(langCode).collect { words ->
                    _learnedWordsCount.value = words.size
                }
            }
        }
        
        viewModelScope.launch {
            userPrefsRepository.userPreferencesFlow.collect { prefs ->
                val langCode = prefs.currentLanguageCode
                repository.getQuizResults(langCode).collect { results ->
                    _quizResults.value = results
                }
            }
        }
    }
}
