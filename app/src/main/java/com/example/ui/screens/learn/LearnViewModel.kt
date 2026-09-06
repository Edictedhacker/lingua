package com.example.ui.screens.learn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.CategoryEntity
import com.example.data.LinguaRepository
import com.example.data.UserPreferencesRepository
import com.example.data.WordEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LearnViewModel(
    private val repository: LinguaRepository,
    private val userPrefsRepository: UserPreferencesRepository
) : ViewModel() {

    private val _categories = MutableStateFlow<List<CategoryEntity>>(emptyList())
    val categories: StateFlow<List<CategoryEntity>> = _categories.asStateFlow()

    private val _words = MutableStateFlow<List<WordEntity>>(emptyList())
    val words: StateFlow<List<WordEntity>> = _words.asStateFlow()
    
    private val _selectedCategory = MutableStateFlow<CategoryEntity?>(null)
    val selectedCategory: StateFlow<CategoryEntity?> = _selectedCategory.asStateFlow()

    init {
        viewModelScope.launch {
            userPrefsRepository.userPreferencesFlow.collect { prefs ->
                repository.getCategories(prefs.currentLanguageCode, "word").collect { cats ->
                    _categories.value = cats
                }
            }
        }
    }
    
    fun selectCategory(category: CategoryEntity) {
        _selectedCategory.value = category
        viewModelScope.launch {
            repository.getWordsByCategory(category.id).collect { list ->
                _words.value = list
            }
        }
    }
    
    fun clearSelection() {
        _selectedCategory.value = null
        _words.value = emptyList()
    }
    
    fun updateWordStatus(word: WordEntity, status: String) {
        viewModelScope.launch {
            repository.updateWord(word.copy(status = status))
            
            if (status == "KNOW_IT") {
                userPrefsRepository.addXp(10)
                userPrefsRepository.recordLearningSession()
            } else if (status == "LEARNING") {
                userPrefsRepository.addXp(5)
                userPrefsRepository.recordLearningSession()
            }
        }
    }
}
