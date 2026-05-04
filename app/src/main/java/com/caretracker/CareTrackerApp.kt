package com.caretracker

import android.app.Application
import com.caretracker.data.db.CareTrackerDatabase
import com.caretracker.data.repository.CareTrackerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

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

    private val _currentUserId = MutableStateFlow(1L)
    val currentUserIdFlow: StateFlow<Long> = _currentUserId

    var currentUserId: Long
        get() = _currentUserId.value
        set(value) { _currentUserId.value = value }
}
