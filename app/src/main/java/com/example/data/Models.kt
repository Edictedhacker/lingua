package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(tableName = "languages")
data class LanguageEntity(
    @PrimaryKey val code: String,
    val name: String,
    val nativeName: String,
    val flagEmoji: String,
    val description: String,
    val difficulty: String
)

@Entity(
    tableName = "categories",
    foreignKeys = [
        ForeignKey(
            entity = LanguageEntity::class,
            parentColumns = ["code"],
            childColumns = ["languageCode"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("languageCode")]
)
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val languageCode: String,
    val name: String, // e.g. "Travel", "Food"
    val type: String // "phrase" or "word"
)

@Entity(
    tableName = "phrases",
    foreignKeys = [
        ForeignKey(
            entity = LanguageEntity::class,
            parentColumns = ["code"],
            childColumns = ["languageCode"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("languageCode"), Index("categoryId")]
)
data class PhraseEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val languageCode: String,
    val categoryId: Int,
    val nativePhrase: String,
    val pronunciation: String,
    val englishTranslation: String,
    val isFavorite: Boolean = false
)

enum class LearningStatus {
    NEW, LEARNING, KNOW_IT, DIFFICULT
}

@Entity(
    tableName = "words",
    foreignKeys = [
        ForeignKey(
            entity = LanguageEntity::class,
            parentColumns = ["code"],
            childColumns = ["languageCode"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("languageCode"), Index("categoryId")]
)
data class WordEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val languageCode: String,
    val categoryId: Int,
    val nativeWord: String,
    val pronunciation: String,
    val translation: String,
    val partOfSpeech: String,
    val exampleSentence: String,
    val isFavorite: Boolean = false,
    
    // Spaced repetition data
    val status: String = LearningStatus.NEW.name,
    val nextReviewTime: Long = 0L,
    val repetitions: Int = 0,
    val easeFactor: Float = 2.5f,
    val intervalDays: Int = 0
)

@Entity(
    tableName = "quiz_results",
    foreignKeys = [
        ForeignKey(
            entity = LanguageEntity::class,
            parentColumns = ["code"],
            childColumns = ["languageCode"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("languageCode")]
)
data class QuizResultEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val languageCode: String,
    val date: Long,
    val score: Int,
    val total: Int,
    val xpEarned: Int
)
