package com.example.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.LinguaRepository
import com.example.data.PhraseEntity
import com.example.data.UserPreferencesRepository
import com.example.data.WordEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: LinguaRepository,
    private val userPrefsRepository: UserPreferencesRepository
) : ViewModel() {
    
    val userPrefs = userPrefsRepository.userPreferencesFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )
    
    private val _dailyPhrase = MutableStateFlow<PhraseEntity?>(null)
    val dailyPhrase: StateFlow<PhraseEntity?> = _dailyPhrase.asStateFlow()
    
    private val _learnedWordsCount = MutableStateFlow(0)
    val learnedWordsCount: StateFlow<Int> = _learnedWordsCount.asStateFlow()
    
    init {
        viewModelScope.launch {
            userPrefsRepository.userPreferencesFlow.collect { prefs ->
                val langCode = prefs.currentLanguageCode
                // Load random phrase
                val phrases = repository.getPhrases(langCode)
                phrases.collect { list ->
                    if (list.isNotEmpty() && _dailyPhrase.value == null) {
                        _dailyPhrase.value = list.random()
                    }
                }
            }
        }
        
        viewModelScope.launch {
            userPrefsRepository.userPreferencesFlow.collect { prefs ->
                val langCode = prefs.currentLanguageCode
                repository.getLearnedWords(langCode).collect { words ->
                    _learnedWordsCount.value = words.size
                }
            }
        }
    }
    
    fun toggleFavorite(phrase: PhraseEntity) {
        viewModelScope.launch {
            repository.updatePhrase(phrase.copy(isFavorite = !phrase.isFavorite))
            if (_dailyPhrase.value?.id == phrase.id) {
                _dailyPhrase.value = phrase.copy(isFavorite = !phrase.isFavorite)
            }
        }
    }
}
