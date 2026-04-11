package com.caretracker.di

import android.content.Context
import androidx.room.Room
import com.caretracker.data.dao.DailyHealthEntryDao
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
    fun provideDatabase(@ApplicationContext context: Context): CareTrackerDatabase =
        Room.databaseBuilder(
            context,
            CareTrackerDatabase::class.java,
            "care_tracker_database"
        )
            .addMigrations(
                CareTrackerDatabase.MIGRATION_2_3,
                CareTrackerDatabase.MIGRATION_3_4,
                CareTrackerDatabase.MIGRATION_4_5,
                CareTrackerDatabase.MIGRATION_5_6
            )
            .build()

    @Provides
    fun providePersonDao(db: CareTrackerDatabase): PersonDao = db.personDao()

    @Provides
    fun provideAppointmentDao(db: CareTrackerDatabase): AppointmentDao = db.appointmentDao()

    @Provides
    fun provideContactDao(db: CareTrackerDatabase): ContactDao = db.contactDao()

    @Provides
    fun provideMedicationDao(db: CareTrackerDatabase): MedicationDao = db.medicationDao()

    @Provides
    fun provideHabitDao(db: CareTrackerDatabase): HabitDao = db.habitDao()

    @Provides
    fun provideHealthLogDao(db: CareTrackerDatabase): HealthLogDao = db.healthLogDao()

    @Provides
    fun provideDoctorDao(db: CareTrackerDatabase): DoctorDao = db.doctorDao()

    @Provides
    fun provideDailyHealthEntryDao(db: CareTrackerDatabase): DailyHealthEntryDao =
        db.dailyHealthEntryDao()
}
