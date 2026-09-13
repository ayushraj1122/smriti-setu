package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.GameAttemptEntity
import com.example.data.model.ReminderEntity
import com.example.data.model.RoutineItemEntity
import com.example.data.model.SettingsEntity
import com.example.data.model.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun getUserById(id: Long): UserEntity?

    @Query("SELECT * FROM users WHERE patientCode = :code AND role = 'PATIENT' LIMIT 1")
    suspend fun getPatientByCode(code: String): UserEntity?

    @Query("SELECT * FROM users WHERE role = 'PATIENT' ORDER BY fullName ASC")
    fun getAllPatients(): Flow<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity): Long

    @Update
    suspend fun updateUser(user: UserEntity)
}

@Dao
interface GameAttemptDao {
    @Query("SELECT * FROM game_attempts WHERE userId = :userId ORDER BY timestamp DESC")
    fun getAttemptsForUser(userId: Long): Flow<List<GameAttemptEntity>>

    @Query("SELECT * FROM game_attempts WHERE userId = :userId AND gameCategory = :category ORDER BY timestamp DESC")
    fun getAttemptsByCategory(userId: Long, category: String): Flow<List<GameAttemptEntity>>

    @Query("SELECT * FROM game_attempts WHERE userId = :userId ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getRecentAttempts(userId: Long, limit: Int = 10): List<GameAttemptEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttempt(attempt: GameAttemptEntity): Long

    @Query("DELETE FROM game_attempts WHERE userId = :userId")
    suspend fun clearUserAttempts(userId: Long)
}

@Dao
interface ReminderDao {
    @Query("SELECT * FROM reminders WHERE patientId = :patientId ORDER BY isCompleted ASC, timeStr ASC")
    fun getRemindersForPatient(patientId: Long): Flow<List<ReminderEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminder(reminder: ReminderEntity): Long

    @Update
    suspend fun updateReminder(reminder: ReminderEntity)

    @Query("DELETE FROM reminders WHERE id = :id")
    suspend fun deleteReminder(id: Long)

    @Query("UPDATE reminders SET isCompleted = :completed WHERE id = :id")
    suspend fun setReminderCompleted(id: Long, completed: Boolean)
}

@Dao
interface DailyRoutineDao {
    @Query("SELECT * FROM daily_routine WHERE patientId = :patientId ORDER BY orderIndex ASC, timeStr ASC")
    fun getRoutineForPatient(patientId: Long): Flow<List<RoutineItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoutineItem(item: RoutineItemEntity): Long

    @Update
    suspend fun updateRoutineItem(item: RoutineItemEntity)

    @Query("DELETE FROM daily_routine WHERE id = :id")
    suspend fun deleteRoutineItem(id: Long)

    @Query("UPDATE daily_routine SET isCompleted = :completed WHERE id = :id")
    suspend fun setRoutineItemCompleted(id: Long, completed: Boolean)
}

@Dao
interface SettingsDao {
    @Query("SELECT * FROM app_settings WHERE userId = :userId LIMIT 1")
    fun getSettingsForUser(userId: Long): Flow<SettingsEntity?>

    @Query("SELECT * FROM app_settings WHERE userId = :userId LIMIT 1")
    suspend fun getSettingsDirect(userId: Long): SettingsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSettings(settings: SettingsEntity)
}
