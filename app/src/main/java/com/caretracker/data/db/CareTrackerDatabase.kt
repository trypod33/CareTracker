package com.caretracker.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.caretracker.data.dao.*
import com.caretracker.data.entities.*

@Database(
    entities = [
        UserEntity::class,
        HabitEntity::class,
        HabitLogEntity::class,
        HealthEntryEntity::class,
        MedicationEntity::class,
        MedLogEntity::class,
        CalendarEventEntity::class,
        TaskEntity::class,
        MoodJournalEntity::class,
        VitalLogEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class CareTrackerDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun habitDao(): HabitDao
    abstract fun healthDao(): HealthDao
    abstract fun medicationDao(): MedicationDao
    abstract fun calendarDao(): CalendarDao
    abstract fun taskDao(): TaskDao
    abstract fun moodDao(): MoodDao

    companion object {
        @Volatile
        private var INSTANCE: CareTrackerDatabase? = null

        fun getDatabase(context: Context): CareTrackerDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    CareTrackerDatabase::class.java,
                    "caretracker_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
