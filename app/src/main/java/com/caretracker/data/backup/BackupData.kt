package com.caretracker.data.backup

import com.caretracker.data.entities.*

data class BackupData(
    val version: Int = 1,
    val exportedAt: Long = System.currentTimeMillis(),
    val user: UserEntity,
    val habits: List<HabitEntity> = emptyList(),
    val habitLogs: List<HabitLogEntity> = emptyList(),
    val medications: List<MedicationEntity> = emptyList(),
    val medLogs: List<MedLogEntity> = emptyList(),
    val tasks: List<TaskEntity> = emptyList(),
    val healthEntries: List<HealthEntryEntity> = emptyList(),
    val vitalLogs: List<VitalLogEntity> = emptyList(),
    val moodEntries: List<MoodJournalEntity> = emptyList(),
    val calendarEvents: List<CalendarEventEntity> = emptyList()
)
