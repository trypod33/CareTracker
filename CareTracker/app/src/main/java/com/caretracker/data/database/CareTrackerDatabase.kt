package com.caretracker.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
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
        HealthLog::class
    ],
    version = 1,
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
}