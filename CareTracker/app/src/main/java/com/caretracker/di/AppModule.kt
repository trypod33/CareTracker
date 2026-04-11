package com.caretracker.di

import android.content.Context
import androidx.room.Room
import com.caretracker.data.dao.DailyHealthEntryDao
import com.caretracker.data.dao.VitalReadingDao
import com.caretracker.data.database.AppointmentDao
import com.caretracker.data.database.CareTrackerDatabase
import com.caretracker.data.database.ContactDao
import com.caretracker.data.database.DoctorDao
import com.caretracker.data.database.HabitDao
import com.caretracker.data.database.HealthLogDao
import com.caretracker.data.database.MedicationDao
import com.caretracker.data.database.PersonDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): CareTrackerDatabase =
        Room.databaseBuilder(
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
            .fallbackToDestructiveMigrationOnDowngrade()
            .build()

    @Provides
    @Singleton
    fun providePersonDao(db: CareTrackerDatabase): PersonDao = db.personDao()

    @Provides
    @Singleton
    fun provideAppointmentDao(db: CareTrackerDatabase): AppointmentDao = db.appointmentDao()

    @Provides
    @Singleton
    fun provideContactDao(db: CareTrackerDatabase): ContactDao = db.contactDao()

    @Provides
    @Singleton
    fun provideMedicationDao(db: CareTrackerDatabase): MedicationDao = db.medicationDao()

    @Provides
    @Singleton
    fun provideHabitDao(db: CareTrackerDatabase): HabitDao = db.habitDao()

    @Provides
    @Singleton
    fun provideHealthLogDao(db: CareTrackerDatabase): HealthLogDao = db.healthLogDao()

    @Provides
    @Singleton
    fun provideDoctorDao(db: CareTrackerDatabase): DoctorDao = db.doctorDao()

    @Provides
    @Singleton
    fun provideDailyHealthEntryDao(db: CareTrackerDatabase): DailyHealthEntryDao =
        db.dailyHealthEntryDao()

    @Provides
    @Singleton
    fun provideVitalReadingDao(db: CareTrackerDatabase): VitalReadingDao =
        db.vitalReadingDao()
}
