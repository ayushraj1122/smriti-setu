package com.example.data.repository

import com.example.data.db.AppDatabase
import com.example.data.model.GameAttemptEntity
import com.example.data.model.ReminderEntity
import com.example.data.model.RoutineItemEntity
import com.example.data.model.SettingsEntity
import com.example.data.model.UserEntity
import kotlinx.coroutines.flow.Flow

class SmritiRepository(private val db: AppDatabase) {

    suspend fun getUserByEmail(email: String): UserEntity? {
        return db.userDao().getUserByEmail(email)
    }

    suspend fun getUserById(id: Long): UserEntity? {
        return db.userDao().getUserById(id)
    }

    suspend fun getPatientByCode(code: String): UserEntity? {
        return db.userDao().getPatientByCode(code)
    }

    fun getAllPatients(): Flow<List<UserEntity>> {
        return db.userDao().getAllPatients()
    }

    suspend fun insertUser(user: UserEntity): Long {
        return db.userDao().insertUser(user)
    }

    suspend fun updateUser(user: UserEntity) {
        db.userDao().updateUser(user)
    }

    // Game Attempts
    fun getAttemptsForUser(userId: Long): Flow<List<GameAttemptEntity>> {
        return db.gameAttemptDao().getAttemptsForUser(userId)
    }

    suspend fun getRecentAttempts(userId: Long, limit: Int = 10): List<GameAttemptEntity> {
        return db.gameAttemptDao().getRecentAttempts(userId, limit)
    }

    suspend fun recordGameAttempt(attempt: GameAttemptEntity): Long {
        return db.gameAttemptDao().insertAttempt(attempt)
    }

    // Reminders
    fun getRemindersForPatient(patientId: Long): Flow<List<ReminderEntity>> {
        return db.reminderDao().getRemindersForPatient(patientId)
    }

    suspend fun addReminder(reminder: ReminderEntity): Long {
        return db.reminderDao().insertReminder(reminder)
    }

    suspend fun updateReminder(reminder: ReminderEntity) {
        db.reminderDao().updateReminder(reminder)
    }

    suspend fun deleteReminder(id: Long) {
        db.reminderDao().deleteReminder(id)
    }

    suspend fun toggleReminder(id: Long, isCompleted: Boolean) {
        db.reminderDao().setReminderCompleted(id, isCompleted)
    }

    // Routine
    fun getRoutineForPatient(patientId: Long): Flow<List<RoutineItemEntity>> {
        return db.dailyRoutineDao().getRoutineForPatient(patientId)
    }

    suspend fun addRoutineItem(item: RoutineItemEntity): Long {
        return db.dailyRoutineDao().insertRoutineItem(item)
    }

    suspend fun updateRoutineItem(item: RoutineItemEntity) {
        db.dailyRoutineDao().updateRoutineItem(item)
    }

    suspend fun deleteRoutineItem(id: Long) {
        db.dailyRoutineDao().deleteRoutineItem(id)
    }

    suspend fun toggleRoutineItem(id: Long, isCompleted: Boolean) {
        db.dailyRoutineDao().setRoutineItemCompleted(id, isCompleted)
    }

    // Settings
    fun getSettings(userId: Long): Flow<SettingsEntity?> {
        return db.settingsDao().getSettingsForUser(userId)
    }

    suspend fun getSettingsDirect(userId: Long): SettingsEntity? {
        return db.settingsDao().getSettingsDirect(userId)
    }

    suspend fun saveSettings(settings: SettingsEntity) {
        db.settingsDao().saveSettings(settings)
    }
}
