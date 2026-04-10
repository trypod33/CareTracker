package com.caretracker.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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
        PersonDoctorCrossRef::class
    ],
    version = 3,
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

    companion object {
        /**
         * Migration 2 → 3:
         *  - Add isActiveProfile column to people table
         *  - Create person_doctor_links join table
         */
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Add isActiveProfile to people (default 0 = false)
                db.execSQL(
                    "ALTER TABLE people ADD COLUMN isActiveProfile INTEGER NOT NULL DEFAULT 0"
                )
                // Create many-to-many join table for person ↔ doctor links
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS person_doctor_links (
                        personId INTEGER NOT NULL,
                        doctorId INTEGER NOT NULL,
                        PRIMARY KEY (personId, doctorId)
                    )
                    """.trimIndent()
                )
                db.execSQL(
                    "CREATE INDEX IF NOT EXISTS index_person_doctor_links_doctorId ON person_doctor_links (doctorId)"
                )
            }
        }
    }
}
