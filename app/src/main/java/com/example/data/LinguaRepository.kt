package com.example.data

import kotlinx.coroutines.flow.Flow

class LinguaRepository(private val dao: LinguaDao) {
    
    // Languages
    val allLanguages: Flow<List<LanguageEntity>> = dao.getAllLanguages()
    suspend fun getLanguage(code: String) = dao.getLanguage(code)
    
    // Categories
    fun getCategories(languageCode: String, type: String) = dao.getCategories(languageCode, type)
    
    // Phrases
    fun getPhrases(languageCode: String) = dao.getPhrases(languageCode)
    fun getPhrasesByCategory(categoryId: Int) = dao.getPhrasesByCategory(categoryId)
    fun getFavoritePhrases() = dao.getFavoritePhrases()
    suspend fun updatePhrase(phrase: PhraseEntity) = dao.updatePhrase(phrase)
    suspend fun searchPhrases(query: String) = dao.searchPhrases(query)
    
    // Words
    fun getWords(languageCode: String) = dao.getWords(languageCode)
    fun getWordsByCategory(categoryId: Int) = dao.getWordsByCategory(categoryId)
    fun getFavoriteWords() = dao.getFavoriteWords()
    fun getLearnedWords(languageCode: String) = dao.getLearnedWords(languageCode)
    
    suspend fun getWordsForReview(languageCode: String, limit: Int) = 
        dao.getWordsForReview(languageCode, System.currentTimeMillis(), limit)
        
    suspend fun getNewWords(languageCode: String, limit: Int) = 
        dao.getNewWords(languageCode, limit)
        
    suspend fun getRandomWords(languageCode: String, limit: Int) =
        dao.getRandomWords(languageCode, limit)
        
    suspend fun updateWord(word: WordEntity) = dao.updateWord(word)
    suspend fun searchWords(query: String) = dao.searchWords(query)
    
    // Quizzes
    fun getQuizResults(languageCode: String) = dao.getQuizResults(languageCode)
    suspend fun insertQuizResult(result: QuizResultEntity) = dao.insertQuizResult(result)
    
    // Seed data helper
    suspend fun populateInitialData(seedHelper: SeedDataHelper) {
        // We will implement this
        val langs = dao.getAllLanguages()
        // If empty, insert
    }
}
