package com.example.ui.screens.phrasebook

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.CategoryEntity
import com.example.data.LinguaRepository
import com.example.data.PhraseEntity
import com.example.data.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PhrasebookViewModel(
    private val repository: LinguaRepository,
    private val userPrefsRepository: UserPreferencesRepository
) : ViewModel() {

    private val _categories = MutableStateFlow<List<CategoryEntity>>(emptyList())
    val categories: StateFlow<List<CategoryEntity>> = _categories.asStateFlow()

    private val _phrases = MutableStateFlow<List<PhraseEntity>>(emptyList())
    val phrases: StateFlow<List<PhraseEntity>> = _phrases.asStateFlow()
    
    private val _selectedCategory = MutableStateFlow<CategoryEntity?>(null)
    val selectedCategory: StateFlow<CategoryEntity?> = _selectedCategory.asStateFlow()

    init {
        viewModelScope.launch {
            userPrefsRepository.userPreferencesFlow.collect { prefs ->
                repository.getCategories(prefs.currentLanguageCode, "phrase").collect { cats ->
                    _categories.value = cats
                }
            }
        }
    }
    
    fun selectCategory(category: CategoryEntity) {
        _selectedCategory.value = category
        viewModelScope.launch {
            repository.getPhrasesByCategory(category.id).collect { list ->
                _phrases.value = list
            }
        }
    }
    
    fun toggleFavorite(phrase: PhraseEntity) {
        viewModelScope.launch {
            repository.updatePhrase(phrase.copy(isFavorite = !phrase.isFavorite))
        }
    }
    
    fun clearSelection() {
        _selectedCategory.value = null
        _phrases.value = emptyList()
    }
}
