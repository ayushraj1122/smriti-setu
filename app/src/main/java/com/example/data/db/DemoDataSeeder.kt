package com.example.data.db

import com.example.data.model.GameAttemptEntity
import com.example.data.model.ReminderEntity
import com.example.data.model.RoutineItemEntity
import com.example.data.model.SettingsEntity
import com.example.data.model.UserEntity

object DemoDataSeeder {
    suspend fun seedDatabaseIfEmpty(db: AppDatabase) {
        val existingPatient = db.userDao().getUserByEmail("patient@demo.com")
        if (existingPatient != null) return

        // 1. Insert Sample Patient
        val patient = UserEntity(
            email = "patient@demo.com",
            passwordHash = "demo123",
            fullName = "Arun Sharma",
            role = "PATIENT",
            age = 68,
            gender = "Male",
            preferredLanguage = "en",
            locationState = "Assam",
            patientCode = "NER-6842"
        )
        val patientId = db.userDao().insertUser(patient)

        // 2. Insert Sample Caregiver
        val caregiver = UserEntity(
            email = "caregiver@demo.com",
            passwordHash = "care123",
            fullName = "Priya Sharma",
            role = "CAREGIVER",
            age = 36,
            gender = "Female",
            preferredLanguage = "en",
            locationState = "Assam",
            linkedPatientId = patientId
        )
        val caregiverId = db.userDao().insertUser(caregiver)

        // 3. Settings for Patient
        val settings = SettingsEntity(
            userId = patientId,
            highContrast = false,
            fontSizeScale = "STANDARD",
            largeButtons = true,
            isDarkMode = false,
            preferredLanguage = "en",
            voiceEnabled = true,
            speechRate = 0.9f
        )
        db.settingsDao().saveSettings(settings)

        // 4. Sample Game Attempts over the last few days
        val now = System.currentTimeMillis()
        val dayMs = 86400000L

        val demoAttempts = listOf(
            GameAttemptEntity(
                userId = patientId,
                gameId = "day_date",
                gameCategory = "orientation",
                level = 1,
                timestamp = now - (dayMs * 3) - 3600000L,
                totalQuestions = 3,
                correctAnswers = 3,
                incorrectAnswers = 0,
                accuracyPercent = 100,
                avgResponseTimeMs = 3800,
                totalScore = 95,
                recommendationMessage = "Outstanding! Ready for Level 2."
            ),
            GameAttemptEntity(
                userId = patientId,
                gameId = "place_time",
                gameCategory = "orientation",
                level = 1,
                timestamp = now - (dayMs * 2) - 7200000L,
                totalQuestions = 3,
                correctAnswers = 2,
                incorrectAnswers = 1,
                accuracyPercent = 67,
                avgResponseTimeMs = 4500,
                totalScore = 75,
                recommendationMessage = "Good effort! Let's practice Level 1 again."
            ),
            GameAttemptEntity(
                userId = patientId,
                gameId = "memory_match",
                gameCategory = "memory",
                level = 1,
                timestamp = now - (dayMs * 2) - 1800000L,
                totalQuestions = 4,
                correctAnswers = 4,
                incorrectAnswers = 0,
                accuracyPercent = 100,
                avgResponseTimeMs = 4100,
                totalScore = 98,
                recommendationMessage = "Excellent memory recall! Advance to Level 2."
            ),
            GameAttemptEntity(
                userId = patientId,
                gameId = "find_target",
                gameCategory = "attention",
                level = 1,
                timestamp = now - dayMs - 5400000L,
                totalQuestions = 3,
                correctAnswers = 3,
                incorrectAnswers = 0,
                accuracyPercent = 100,
                avgResponseTimeMs = 3200,
                totalScore = 96,
                recommendationMessage = "Great focus! Ready for Level 2."
            ),
            GameAttemptEntity(
                userId = patientId,
                gameId = "simple_pattern",
                gameCategory = "reasoning",
                level = 1,
                timestamp = now - dayMs - 2700000L,
                totalQuestions = 3,
                correctAnswers = 2,
                incorrectAnswers = 1,
                accuracyPercent = 67,
                avgResponseTimeMs = 5200,
                totalScore = 72,
                recommendationMessage = "Good practice! Continue with Level 1."
            ),
            GameAttemptEntity(
                userId = patientId,
                gameId = "remember_objects",
                gameCategory = "memory",
                level = 1,
                timestamp = now - 3600000L,
                totalQuestions = 3,
                correctAnswers = 3,
                incorrectAnswers = 0,
                accuracyPercent = 100,
                avgResponseTimeMs = 3500,
                totalScore = 94,
                recommendationMessage = "Great job remembering objects! Try Level 2."
            )
        )

        demoAttempts.forEach { db.gameAttemptDao().insertAttempt(it) }

        // 5. Pre-populate Reminders
        val demoReminders = listOf(
            ReminderEntity(
                patientId = patientId,
                title = "Morning Blood Pressure Medicine",
                description = "Take with warm water after breakfast",
                category = "MEDICATION",
                timeStr = "08:30 AM",
                dateStr = "Daily",
                repeatFrequency = "DAILY",
                isActive = true,
                isCompleted = true
            ),
            ReminderEntity(
                patientId = patientId,
                title = "Hydration Break - Fresh Water",
                description = "Drink 1 glass of fresh filtered water",
                category = "HYDRATION",
                timeStr = "11:00 AM",
                dateStr = "Daily",
                repeatFrequency = "DAILY",
                isActive = true,
                isCompleted = false
            ),
            ReminderEntity(
                patientId = patientId,
                title = "Afternoon Cognitive Game Session",
                description = "Enjoy 10 minutes of Day & Date game",
                category = "GAME_PRACTICE",
                timeStr = "03:30 PM",
                dateStr = "Daily",
                repeatFrequency = "DAILY",
                isActive = true,
                isCompleted = false
            ),
            ReminderEntity(
                patientId = patientId,
                title = "Gentle Garden Stroll",
                description = "15-minute gentle walk in garden with family",
                category = "EXERCISE",
                timeStr = "05:00 PM",
                dateStr = "Daily",
                repeatFrequency = "DAILY",
                isActive = true,
                isCompleted = false
            )
        )

        demoReminders.forEach { db.reminderDao().insertReminder(it) }

        // 6. Pre-populate Daily Routine Timeline
        val demoRoutine = listOf(
            RoutineItemEntity(
                patientId = patientId,
                period = "MORNING",
                timeStr = "07:30 AM",
                title = "Wake Up & Light Stretching",
                description = "Gentle breathing and natural daylight",
                isCompleted = true,
                orderIndex = 1
            ),
            RoutineItemEntity(
                patientId = patientId,
                period = "MORNING",
                timeStr = "08:15 AM",
                title = "Wholesome Breakfast",
                description = "Warm Assam tea and light porridge",
                isCompleted = true,
                orderIndex = 2
            ),
            RoutineItemEntity(
                patientId = patientId,
                period = "MORNING",
                timeStr = "09:30 AM",
                title = "Morning Cognitive Activity",
                description = "Play Memory Match or Orientation Game",
                isCompleted = true,
                orderIndex = 3
            ),
            RoutineItemEntity(
                patientId = patientId,
                period = "AFTERNOON",
                timeStr = "01:00 PM",
                title = "Lunch & Hydration",
                description = "Balanced meal with fresh greens",
                isCompleted = false,
                orderIndex = 4
            ),
            RoutineItemEntity(
                patientId = patientId,
                period = "AFTERNOON",
                timeStr = "02:30 PM",
                title = "Quiet Rest & Music",
                description = "Restful quiet time or soothing folk flute",
                isCompleted = false,
                orderIndex = 5
            ),
            RoutineItemEntity(
                patientId = patientId,
                period = "EVENING",
                timeStr = "05:00 PM",
                title = "Evening Walk & Social Talk",
                description = "Connecting with family & neighbors",
                isCompleted = false,
                orderIndex = 6
            ),
            RoutineItemEntity(
                patientId = patientId,
                period = "EVENING",
                timeStr = "08:00 PM",
                title = "Light Dinner & Winding Down",
                description = "Peaceful evening preparation",
                isCompleted = false,
                orderIndex = 7
            )
        )

        demoRoutine.forEach { db.dailyRoutineDao().insertRoutineItem(it) }
    }
}
