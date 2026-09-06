package com.example

import android.app.Application
import com.example.data.LinguaDatabase
import com.example.data.LinguaRepository
import com.example.data.SeedDataHelper
import com.example.data.UserPreferencesRepository
import com.example.data.dataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class LinguaApplication : Application() {
    
    val applicationScope = CoroutineScope(SupervisorJob())
    
    val database by lazy { LinguaDatabase.getDatabase(this) }
    val repository by lazy { LinguaRepository(database.linguaDao()) }
    val userPreferencesRepository by lazy { UserPreferencesRepository(dataStore) }
    
    override fun onCreate() {
        super.onCreate()
        
        // Populate initial data on first launch
        applicationScope.launch {
            val languages = repository.getLanguage("es")
            if (languages == null) {
                SeedDataHelper(database.linguaDao()).populateDatabase()
            }
        }
    }
}
