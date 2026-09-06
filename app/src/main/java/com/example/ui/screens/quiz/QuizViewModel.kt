package com.example.ui.screens.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.LinguaRepository
import com.example.data.UserPreferencesRepository
import com.example.data.WordEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QuizViewModel(
    private val repository: LinguaRepository,
    private val userPrefsRepository: UserPreferencesRepository
) : ViewModel() {

    private val _quizWords = MutableStateFlow<List<WordEntity>>(emptyList())
    
    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex: StateFlow<Int> = _currentQuestionIndex.asStateFlow()
    
    private val _options = MutableStateFlow<List<String>>(emptyList())
    val options: StateFlow<List<String>> = _options.asStateFlow()
    
    private val _currentWord = MutableStateFlow<WordEntity?>(null)
    val currentWord: StateFlow<WordEntity?> = _currentWord.asStateFlow()
    
    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score.asStateFlow()
    
    private val _isFinished = MutableStateFlow(false)
    val isFinished: StateFlow<Boolean> = _isFinished.asStateFlow()
    
    private val _selectedAnswer = MutableStateFlow<String?>(null)
    val selectedAnswer: StateFlow<String?> = _selectedAnswer.asStateFlow()
    
    fun startQuiz(languageCode: String) {
        viewModelScope.launch {
            repository.getWordsByCategory(0).collect { allWords ->
                // In a real app we'd fetch words across all categories
            }
        }
    }
    
    fun loadRandomQuiz() {
        viewModelScope.launch {
            userPrefsRepository.userPreferencesFlow.collect { prefs ->
                val langCode = prefs.currentLanguageCode
                repository.getWords(langCode).collect { allWords ->
                    if (allWords.size >= 4) {
                        val shuffled = allWords.shuffled()
                        _quizWords.value = shuffled.take(5) // 5 questions
                        _currentQuestionIndex.value = 0
                        _score.value = 0
                        _isFinished.value = false
                        setupNextQuestion(allWords)
                    } else {
                        // Not enough words to quiz
                        _isFinished.value = true
                    }
                }
            }
        }
    }
    
    private fun setupNextQuestion(allWords: List<WordEntity>) {
        if (_currentQuestionIndex.value < _quizWords.value.size) {
            val word = _quizWords.value[_currentQuestionIndex.value]
            _currentWord.value = word
            
            // Generate options
            val distractors = allWords.filter { it.id != word.id }
                .shuffled()
                .take(3)
                .map { it.translation }
                
            _options.value = (distractors + word.translation).shuffled()
            _selectedAnswer.value = null
        } else {
            _isFinished.value = true
            viewModelScope.launch {
                userPrefsRepository.addXp(_score.value * 5)
                userPrefsRepository.recordLearningSession()
            }
        }
    }
    
    fun selectAnswer(answer: String, allWords: List<WordEntity> = emptyList()) {
        if (_selectedAnswer.value != null) return
        
        _selectedAnswer.value = answer
        
        if (answer == _currentWord.value?.translation) {
            _score.value += 1
        }
    }
    
    fun nextQuestion() {
        viewModelScope.launch {
            userPrefsRepository.userPreferencesFlow.collect { prefs ->
                val langCode = prefs.currentLanguageCode
                repository.getWords(langCode).collect { allWords ->
                    _currentQuestionIndex.value += 1
                    setupNextQuestion(allWords)
                }
            }
        }
    }
}
