package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        LanguageEntity::class,
        CategoryEntity::class,
        PhraseEntity::class,
        WordEntity::class,
        QuizResultEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class LinguaDatabase : RoomDatabase() {
    abstract fun linguaDao(): LinguaDao

    companion object {
        @Volatile
        private var INSTANCE: LinguaDatabase? = null

        fun getDatabase(context: Context): LinguaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LinguaDatabase::class.java,
                    "lingua_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
