package com.caretracker.di

import android.content.Context
import androidx.room.Room
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
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): CareTrackerDatabase =
        Room.databaseBuilder(
            context,
            CareTrackerDatabase::class.java,
            "care_tracker_db"
        )
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideDoctorDao(db: CareTrackerDatabase): DoctorDao = db.doctorDao()

    @Provides
    fun provideMedicationDao(db: CareTrackerDatabase): MedicationDao = db.medicationDao()

    @Provides
    fun providePersonDao(db: CareTrackerDatabase): PersonDao = db.personDao()

    @Provides
    fun provideAppointmentDao(db: CareTrackerDatabase): AppointmentDao = db.appointmentDao()

    @Provides
    fun provideHealthLogDao(db: CareTrackerDatabase): HealthLogDao = db.healthLogDao()

    @Provides
    fun provideHabitDao(db: CareTrackerDatabase): HabitDao = db.habitDao()

    @Provides
    fun provideContactDao(db: CareTrackerDatabase): ContactDao = db.contactDao()
}
