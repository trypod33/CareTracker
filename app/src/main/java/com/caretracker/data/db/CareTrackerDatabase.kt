package com.caretracker.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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
        VitalLogEntity::class,
        BloodPressureReadingEntity::class,
        BloodSugarReadingEntity::class
    ],
    version = 5,
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
    abstract fun bloodPressureDao(): BloodPressureDao
    abstract fun bloodSugarDao(): BloodSugarDao

    companion object {
        @Volatile
        private var INSTANCE: CareTrackerDatabase? = null

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS blood_pressure_readings (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        entryId INTEGER NOT NULL DEFAULT 0,
                        systolic INTEGER NOT NULL,
                        diastolic INTEGER NOT NULL,
                        readingTime TEXT NOT NULL DEFAULT '',
                        label TEXT NOT NULL DEFAULT '',
                        FOREIGN KEY(entryId) REFERENCES health_entries(id) ON DELETE CASCADE
                    )
                """.trimIndent())
                database.execSQL("""
                    CREATE INDEX IF NOT EXISTS index_blood_pressure_readings_entryId
                    ON blood_pressure_readings(entryId)
                """.trimIndent())
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS blood_sugar_readings (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        entryId INTEGER NOT NULL DEFAULT 0,
                        value REAL NOT NULL,
                        readingTime TEXT NOT NULL DEFAULT '',
                        label TEXT NOT NULL DEFAULT '',
                        FOREIGN KEY(entryId) REFERENCES health_entries(id) ON DELETE CASCADE
                    )
                """.trimIndent())
                database.execSQL("""
                    CREATE INDEX IF NOT EXISTS index_blood_sugar_readings_entryId
                    ON blood_sugar_readings(entryId)
                """.trimIndent())
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE habits ADD COLUMN sortOrder INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE tasks ADD COLUMN sortOrder INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE medications ADD COLUMN sortOrder INTEGER NOT NULL DEFAULT 0")
            }
        }

        fun getDatabase(context: Context): CareTrackerDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    CareTrackerDatabase::class.java,
                    "caretracker_db"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
