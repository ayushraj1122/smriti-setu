package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.GameAttemptEntity
import com.example.data.model.ReminderEntity
import com.example.data.model.RoutineItemEntity
import com.example.data.model.SettingsEntity
import com.example.data.model.UserEntity

@Database(
    entities = [
        UserEntity::class,
        GameAttemptEntity::class,
        ReminderEntity::class,
        RoutineItemEntity::class,
        SettingsEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun gameAttemptDao(): GameAttemptDao
    abstract fun reminderDao(): ReminderDao
    abstract fun dailyRoutineDao(): DailyRoutineDao
    abstract fun settingsDao(): SettingsDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "smriti_ner_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
