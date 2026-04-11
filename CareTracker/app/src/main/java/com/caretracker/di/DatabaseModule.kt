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
    fun provideDatabase(@ApplicationContext context: Context): CareTrackerDatabase =
        Room.databaseBuilder(
            context,
            CareTrackerDatabase::class.java,
            "caretracker.db"
        )
        .addMigrations(
            CareTrackerDatabase.MIGRATION_2_3,
            CareTrackerDatabase.MIGRATION_3_4,
            CareTrackerDatabase.MIGRATION_4_5,
            CareTrackerDatabase.MIGRATION_5_6,
            CareTrackerDatabase.MIGRATION_6_7
        )
        .build()

    @Provides fun providePersonDao(db: CareTrackerDatabase)       = db.personDao()
    @Provides fun provideAppointmentDao(db: CareTrackerDatabase)  = db.appointmentDao()
    @Provides fun provideContactDao(db: CareTrackerDatabase)      = db.contactDao()
    @Provides fun provideMedicationDao(db: CareTrackerDatabase)   = db.medicationDao()
    @Provides fun provideHabitDao(db: CareTrackerDatabase)        = db.habitDao()
    @Provides fun provideHealthLogDao(db: CareTrackerDatabase)    = db.healthLogDao()
    @Provides fun provideDoctorDao(db: CareTrackerDatabase)       = db.doctorDao()
    @Provides fun provideDailyHealthEntryDao(db: CareTrackerDatabase): DailyHealthEntryDao = db.dailyHealthEntryDao()
    @Provides fun provideVitalReadingDao(db: CareTrackerDatabase): VitalReadingDao         = db.vitalReadingDao()
}
