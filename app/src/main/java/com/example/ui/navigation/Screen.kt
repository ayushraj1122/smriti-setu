package com.example.ui.navigation

sealed class Screen {
    object Splash : Screen()
    object Welcome : Screen()
    object PatientLogin : Screen()
    object PatientSignUp : Screen()
    object CaregiverLogin : Screen()
    
    data class PatientMain(val initialTab: PatientTab = PatientTab.HOME) : Screen()
    data class CaregiverMain(val initialTab: CaregiverTab = CaregiverTab.DASHBOARD) : Screen()

    data class GamePlay(val gameId: String, val level: Int) : Screen()
    data class GameResult(
        val gameId: String,
        val level: Int,
        val score: Int,
        val accuracy: Int,
        val avgTimeMs: Long,
        val correct: Int,
        val total: Int,
        val recommendation: String
    ) : Screen()
}

enum class PatientTab {
    HOME,
    GAMES,
    PROGRESS,
    PROFILE
}

enum class CaregiverTab {
    DASHBOARD,
    PROGRESS,
    REMINDERS,
    ROUTINE,
    SETTINGS
}
