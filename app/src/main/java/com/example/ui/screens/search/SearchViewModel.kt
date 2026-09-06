package com.example.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.LinguaRepository
import com.example.data.PhraseEntity
import com.example.data.UserPreferencesRepository
import com.example.data.WordEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repository: LinguaRepository,
    private val userPrefsRepository: UserPreferencesRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    private val _phraseResults = MutableStateFlow<List<PhraseEntity>>(emptyList())
    val phraseResults: StateFlow<List<PhraseEntity>> = _phraseResults.asStateFlow()
    
    private val _wordResults = MutableStateFlow<List<WordEntity>>(emptyList())
    val wordResults: StateFlow<List<WordEntity>> = _wordResults.asStateFlow()

    fun onQueryChange(query: String) {
        _searchQuery.value = query
        if (query.length > 2) {
            viewModelScope.launch {
                val phrases = repository.searchPhrases(query)
                _phraseResults.value = phrases
                
                val words = repository.searchWords(query)
                _wordResults.value = words
            }
        } else {
            _phraseResults.value = emptyList()
            _wordResults.value = emptyList()
        }
    }
}
