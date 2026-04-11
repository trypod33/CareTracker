package com.caretracker.utils

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Lightweight in-memory + SharedPreferences session holder.
 *
 * All fragments/ViewModels that need the current person ID should
 * inject SessionManager and call [activePersonId].
 *
 * The value is persisted across app restarts via SharedPreferences.
 * The DB remains the source of truth for profile metadata; this just
 * caches the active ID so we don't need an async DB call on every screen.
 *
 * Thread-safety note: [activePersonId] setter uses commit() (synchronous)
 * rather than apply() (asynchronous) to prevent race conditions when the
 * value is written and then immediately read back on a different coroutine.
 */
@Singleton
class SessionManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    /** The ID of the currently active person profile. -1 means none selected yet. */
    var activePersonId: Long
        get() = prefs.getLong(KEY_ACTIVE_PERSON_ID, -1L)
        set(value) {
            // Use commit() (synchronous, returns Boolean) instead of apply()
            // so rapid successive reads after a write always see the new value.
            prefs.edit().putLong(KEY_ACTIVE_PERSON_ID, value).commit()
        }

    /** True once at least one person has been set as active. */
    val hasActivePerson: Boolean
        get() = activePersonId != -1L

    fun clear() {
        prefs.edit().remove(KEY_ACTIVE_PERSON_ID).commit()
    }

    companion object {
        private const val PREFS_NAME = "care_tracker_session"
        private const val KEY_ACTIVE_PERSON_ID = "active_person_id"
    }
}
