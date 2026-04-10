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
    version = 4,
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
        /** Migration 2 → 3: add isActiveProfile + person_doctor_links join table */
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE people ADD COLUMN isActiveProfile INTEGER NOT NULL DEFAULT 0"
                )
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

        /** Migration 3 → 4: add avatar column to doctors table */
        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE doctors ADD COLUMN avatar TEXT NOT NULL DEFAULT '\uD83E\uDE7A'"
                )
            }
        }
    }
}
