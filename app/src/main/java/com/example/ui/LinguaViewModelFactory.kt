package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.data.LinguaRepository
import com.example.data.UserPreferencesRepository
import com.example.ui.screens.MainViewModel
import com.example.ui.screens.home.HomeViewModel
import com.example.ui.screens.learn.LearnViewModel
import com.example.ui.screens.phrasebook.PhrasebookViewModel
import com.example.ui.screens.progress.ProgressViewModel

class LinguaViewModelFactory(
    private val repository: LinguaRepository,
    private val userPrefsRepository: UserPreferencesRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository, userPrefsRepository) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository, userPrefsRepository) as T
            }
            modelClass.isAssignableFrom(PhrasebookViewModel::class.java) -> {
                PhrasebookViewModel(repository, userPrefsRepository) as T
            }
            modelClass.isAssignableFrom(LearnViewModel::class.java) -> {
                LearnViewModel(repository, userPrefsRepository) as T
            }
            modelClass.isAssignableFrom(ProgressViewModel::class.java) -> {
                ProgressViewModel(repository, userPrefsRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
