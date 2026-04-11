package com.caretracker.utils

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Singleton session holder.
 *
 * [activePersonId] is persisted in SharedPreferences AND exposed as a
 * [StateFlow] so any fragment / ViewModel can react to changes immediately
 * without needing a detach/reattach cycle.
 */
@Singleton
class SessionManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    // In-memory StateFlow — initialized from prefs so it survives process death
    private val _activePersonId = MutableStateFlow(prefs.getLong(KEY_ACTIVE_PERSON_ID, -1L))

    /** Reactive stream of the currently selected person ID. -1 = none selected. */
    val activePersonIdFlow: StateFlow<Long> = _activePersonId.asStateFlow()

    /** The ID of the currently active person profile. -1 means none selected yet. */
    var activePersonId: Long
        get() = _activePersonId.value
        set(value) {
            _activePersonId.value = value
            // Persist synchronously so it survives process kill
            prefs.edit().putLong(KEY_ACTIVE_PERSON_ID, value).commit()
        }

    /** True once at least one person has been set as active. */
    val hasActivePerson: Boolean
        get() = activePersonId != -1L

    fun clear() {
        activePersonId = -1L
        prefs.edit().remove(KEY_ACTIVE_PERSON_ID).commit()
    }

    companion object {
        private const val PREFS_NAME = "care_tracker_session"
        private const val KEY_ACTIVE_PERSON_ID = "active_person_id"
    }
}
