package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val email: String,
    val passwordHash: String,
    val fullName: String,
    val role: String, // PATIENT or CAREGIVER
    val age: Int = 0,
    val gender: String = "Not Specified",
    val preferredLanguage: String = "en",
    val locationState: String = "Assam",
    val linkedPatientId: Long? = null,
    val patientCode: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "game_attempts")
data class GameAttemptEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val gameId: String,
    val gameCategory: String,
    val level: Int,
    val timestamp: Long = System.currentTimeMillis(),
    val totalQuestions: Int,
    val correctAnswers: Int,
    val incorrectAnswers: Int,
    val accuracyPercent: Int,
    val avgResponseTimeMs: Long,
    val totalScore: Int,
    val completionStatus: String = "COMPLETED",
    val recommendationMessage: String = ""
)

@Entity(tableName = "reminders")
data class ReminderEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val patientId: Long,
    val title: String,
    val description: String = "",
    val category: String, // ReminderCategory
    val timeStr: String, // e.g. "08:30 AM"
    val dateStr: String = "Daily",
    val repeatFrequency: String = "DAILY",
    val isActive: Boolean = true,
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "daily_routine")
data class RoutineItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val patientId: Long,
    val period: String, // MORNING, AFTERNOON, EVENING
    val timeStr: String, // e.g. "07:30 AM"
    val title: String,
    val description: String = "",
    val isCompleted: Boolean = false,
    val orderIndex: Int = 0
)

@Entity(tableName = "app_settings")
data class SettingsEntity(
    @PrimaryKey val userId: Long,
    val highContrast: Boolean = false,
    val fontSizeScale: String = "STANDARD",
    val largeButtons: Boolean = true,
    val isDarkMode: Boolean = false,
    val preferredLanguage: String = "en",
    val voiceEnabled: Boolean = true,
    val speechRate: Float = 0.9f // slightly slower for dementia readability
)
