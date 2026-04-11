package com.caretracker.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.caretracker.data.dao.DailyHealthEntryDao
import com.caretracker.data.dao.VitalReadingDao
import com.caretracker.data.models.*

@Database(
    entities = [
        Person::class,
        Appointment::class,
        Contact::class,
        Medication::class,
        MedicationLog::class,
        Habit::class,
        HabitLog::class,
        HealthLog::class,
        Doctor::class,
        PersonDoctorCrossRef::class,
        DailyHealthEntry::class,
        VitalReading::class          // ← added
    ],
    version = 7,                     // ← bumped from 6
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class CareTrackerDatabase : RoomDatabase() {
    abstract fun personDao(): PersonDao
    abstract fun appointmentDao(): AppointmentDao
    abstract fun contactDao(): ContactDao
    abstract fun medicationDao(): MedicationDao
    abstract fun habitDao(): HabitDao
    abstract fun healthLogDao(): HealthLogDao
    abstract fun doctorDao(): DoctorDao
    abstract fun dailyHealthEntryDao(): DailyHealthEntryDao
    abstract fun vitalReadingDao(): VitalReadingDao   // ← added

    companion object {
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE people ADD COLUMN isActiveProfile INTEGER NOT NULL DEFAULT 0")
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS person_doctor_links (
                        personId INTEGER NOT NULL,
                        doctorId INTEGER NOT NULL,
                        PRIMARY KEY (personId, doctorId)
                    )
                """.trimIndent())
                db.execSQL("CREATE INDEX IF NOT EXISTS index_person_doctor_links_doctorId ON person_doctor_links (doctorId)")
            }
        }

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE doctors ADD COLUMN avatar TEXT NOT NULL DEFAULT '\uD83E\uDE7A'")
            }
        }

        val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS habit_logs_new (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        habitId INTEGER NOT NULL,
                        personId INTEGER NOT NULL,
                        logDate TEXT NOT NULL DEFAULT '',
                        value REAL NOT NULL DEFAULT 1.0,
                        notes TEXT NOT NULL DEFAULT '',
                        loggedAt INTEGER NOT NULL
                    )
                """.trimIndent())
                db.execSQL("""
                    INSERT INTO habit_logs_new (id, habitId, personId, logDate, value, notes, loggedAt)
                    SELECT id, habitId, personId, logDate, CAST(value AS REAL), notes, loggedAt
                    FROM habit_logs
                """.trimIndent())
                db.execSQL("DROP TABLE habit_logs")
                db.execSQL("ALTER TABLE habit_logs_new RENAME TO habit_logs")
            }
        }

        val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS `daily_health_entries` (
                        `id`              INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `personId`        INTEGER NOT NULL,
                        `date`            TEXT NOT NULL,
                        `weightValue`     REAL,
                        `weightUnit`      TEXT,
                        `heartRate`       INTEGER,
                        `bpSystolic`      INTEGER,
                        `bpDiastolic`     INTEGER,
                        `bloodSugar`      REAL,
                        `bpTimestamp`     INTEGER,
                        `bsTimestamp`     INTEGER,
                        `sleepHours`      REAL,
                        `sleepQuality`    INTEGER,
                        `mood`            INTEGER,
                        `energyLevel`     INTEGER,
                        `steps`           INTEGER,
                        `exerciseMinutes` INTEGER,
                        `waterOz`         REAL,
                        `calories`        INTEGER,
                        `notes`           TEXT,
                        `createdAt`       INTEGER NOT NULL DEFAULT 0
                    )
                """.trimIndent())
                db.execSQL(
                    "CREATE INDEX IF NOT EXISTS `index_daily_health_entries_personId` " +
                    "ON `daily_health_entries` (`personId`)"
                )
            }
        }

        // Migration 6 → 7: add vital_readings table for timestamped individual readings
        val MIGRATION_6_7 = object : Migration(6, 7) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS `vital_readings` (
                        `id`               INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `personId`         INTEGER NOT NULL,
                        `readingDate`      TEXT NOT NULL,
                        `readingTimestamp` INTEGER NOT NULL,
                        `readingType`      TEXT NOT NULL,
                        `bpSystolic`       INTEGER,
                        `bpDiastolic`      INTEGER,
                        `bpPosition`       TEXT,
                        `bloodSugar`       REAL,
                        `bsContext`        TEXT,
                        `heartRate`        INTEGER,
                        `hrContext`        TEXT,
                        `weightValue`      REAL,
                        `weightUnit`       TEXT,
                        `oxygenSat`        INTEGER,
                        `temperature`      REAL,
                        `tempUnit`         TEXT,
                        `notes`            TEXT,
                        `createdAt`        INTEGER NOT NULL,
                        FOREIGN KEY(`personId`) REFERENCES `people`(`id`) ON DELETE CASCADE
                    )
                """.trimIndent())
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_vital_readings_personId` ON `vital_readings` (`personId`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_vital_readings_readingType` ON `vital_readings` (`readingType`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_vital_readings_readingDate` ON `vital_readings` (`readingDate`)")
            }
        }
    }
}
