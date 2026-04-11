package com.caretracker.di

import android.content.Context
import androidx.room.Room
import com.caretracker.data.dao.DailyHealthEntryDao
import com.caretracker.data.dao.VitalReadingDao
import com.caretracker.data.database.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): CareTrackerDatabase {
        return Room.databaseBuilder(
            context,
            CareTrackerDatabase::class.java,
            "care_tracker_db"
        )
            .addMigrations(
                CareTrackerDatabase.MIGRATION_2_3,
                CareTrackerDatabase.MIGRATION_3_4,
                CareTrackerDatabase.MIGRATION_4_5,
                CareTrackerDatabase.MIGRATION_5_6,
                CareTrackerDatabase.MIGRATION_6_7
            )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun providePersonDao(database: CareTrackerDatabase): PersonDao = database.personDao()

    @Provides
    fun provideAppointmentDao(database: CareTrackerDatabase): AppointmentDao = database.appointmentDao()

    @Provides
    fun provideContactDao(database: CareTrackerDatabase): ContactDao = database.contactDao()

    @Provides
    fun provideMedicationDao(database: CareTrackerDatabase): MedicationDao = database.medicationDao()

    @Provides
    fun provideHabitDao(database: CareTrackerDatabase): HabitDao = database.habitDao()

    @Provides
    fun provideHealthLogDao(database: CareTrackerDatabase): HealthLogDao = database.healthLogDao()

    @Provides
    fun provideDoctorDao(database: CareTrackerDatabase): DoctorDao = database.doctorDao()

    @Provides
    fun provideDailyHealthEntryDao(database: CareTrackerDatabase): DailyHealthEntryDao =
        database.dailyHealthEntryDao()

    @Provides
    fun provideVitalReadingDao(database: CareTrackerDatabase): VitalReadingDao =
        database.vitalReadingDao()
}
