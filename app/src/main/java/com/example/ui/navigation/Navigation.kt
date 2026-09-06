package com.example.ui.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Onboarding : Screen("onboarding")
    object Home : Screen("home")
    object Phrasebook : Screen("phrasebook")
    object Learn : Screen("learn")
    object Progress : Screen("progress")
    object Settings : Screen("settings")
    object LanguageSelection : Screen("language_selection")
    object Vocabulary : Screen("vocabulary")
    object Flashcards : Screen("flashcards")
    object Quiz : Screen("quiz")
    object QuizResults : Screen("quiz_results")
}
