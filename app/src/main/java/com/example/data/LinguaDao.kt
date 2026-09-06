package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface LinguaDao {
    // Languages
    @Query("SELECT * FROM languages")
    fun getAllLanguages(): Flow<List<LanguageEntity>>

    @Query("SELECT * FROM languages WHERE code = :code")
    suspend fun getLanguage(code: String): LanguageEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLanguages(languages: List<LanguageEntity>)

    // Categories
    @Query("SELECT * FROM categories WHERE languageCode = :languageCode AND type = :type")
    fun getCategories(languageCode: String, type: String): Flow<List<CategoryEntity>>
    
    @Query("SELECT * FROM categories WHERE languageCode = :languageCode AND type = :type")
    suspend fun getCategoriesSync(languageCode: String, type: String): List<CategoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categories: List<CategoryEntity>): List<Long>

    // Phrases
    @Query("SELECT * FROM phrases WHERE languageCode = :languageCode")
    fun getPhrases(languageCode: String): Flow<List<PhraseEntity>>

    @Query("SELECT * FROM phrases WHERE categoryId = :categoryId")
    fun getPhrasesByCategory(categoryId: Int): Flow<List<PhraseEntity>>

    @Query("SELECT * FROM phrases WHERE isFavorite = 1")
    fun getFavoritePhrases(): Flow<List<PhraseEntity>>

    @Update
    suspend fun updatePhrase(phrase: PhraseEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhrases(phrases: List<PhraseEntity>)
    
    @Query("SELECT * FROM phrases WHERE nativePhrase LIKE '%' || :query || '%' OR englishTranslation LIKE '%' || :query || '%'")
    suspend fun searchPhrases(query: String): List<PhraseEntity>

    // Words
    @Query("SELECT * FROM words WHERE languageCode = :languageCode")
    fun getWords(languageCode: String): Flow<List<WordEntity>>

    @Query("SELECT * FROM words WHERE categoryId = :categoryId")
    fun getWordsByCategory(categoryId: Int): Flow<List<WordEntity>>
    
    @Query("SELECT * FROM words WHERE languageCode = :languageCode AND status != 'NEW'")
    fun getLearnedWords(languageCode: String): Flow<List<WordEntity>>

    @Query("SELECT * FROM words WHERE isFavorite = 1")
    fun getFavoriteWords(): Flow<List<WordEntity>>
    
    @Query("SELECT * FROM words WHERE languageCode = :languageCode AND status != 'NEW' AND nextReviewTime <= :currentTime ORDER BY nextReviewTime ASC LIMIT :limit")
    suspend fun getWordsForReview(languageCode: String, currentTime: Long, limit: Int): List<WordEntity>
    
    @Query("SELECT * FROM words WHERE languageCode = :languageCode AND status = 'NEW' LIMIT :limit")
    suspend fun getNewWords(languageCode: String, limit: Int): List<WordEntity>
    
    @Query("SELECT * FROM words WHERE languageCode = :languageCode ORDER BY RANDOM() LIMIT :limit")
    suspend fun getRandomWords(languageCode: String, limit: Int): List<WordEntity>

    @Update
    suspend fun updateWord(word: WordEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWords(words: List<WordEntity>)
    
    @Query("SELECT * FROM words WHERE nativeWord LIKE '%' || :query || '%' OR translation LIKE '%' || :query || '%'")
    suspend fun searchWords(query: String): List<WordEntity>

    // Quiz Results
    @Insert
    suspend fun insertQuizResult(result: QuizResultEntity)
    
    @Query("SELECT * FROM quiz_results WHERE languageCode = :languageCode")
    fun getQuizResults(languageCode: String): Flow<List<QuizResultEntity>>
}
