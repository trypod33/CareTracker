package com.caretracker

import android.app.Application
import com.caretracker.data.db.CareTrackerDatabase
import com.caretracker.data.repository.CareTrackerRepository

class CareTrackerApp : Application() {
    val database by lazy { CareTrackerDatabase.getDatabase(this) }
    val repository by lazy {
        CareTrackerRepository(
            database.userDao(),
            database.habitDao(),
            database.healthDao(),
            database.medicationDao(),
            database.calendarDao(),
            database.taskDao(),
            database.moodDao()
        )
    }
}
