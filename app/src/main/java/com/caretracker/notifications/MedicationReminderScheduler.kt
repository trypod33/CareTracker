package com.caretracker.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.caretracker.data.entities.MedicationEntity
import java.util.Calendar

object MedicationReminderScheduler {

    private const val PREFS_NAME = "caretracker"
    private const val KEY_REMINDERS_ENABLED = "reminders_enabled"
    private const val KEY_MED_REMINDERS_ENABLED = "med_reminders_enabled"
    private const val KEY_DEFAULT_REMINDER_HOUR = "default_reminder_hour"
    private const val KEY_DEFAULT_REMINDER_MINUTE = "default_reminder_minute"
    private const val REQUEST_CODE_BASE = 20000
    private const val MAX_REMINDER_SLOTS = 8

    fun schedule(context: Context, medication: MedicationEntity) {
        cancel(context, medication)

        if (!medication.isActive) return
        if (!areMedicationRemindersAllowed(context)) return

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
            return
        }

        val times = resolveReminderTimes(context, medication)
        times.forEachIndexed { index, pair ->
            val triggerAt = resolveTriggerAtMillis(pair.first, pair.second)
            val pendingIntent = buildPendingIntent(context, medication, index, pair.first, pair.second)

            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerAt,
                pendingIntent
            )
        }
    }

    fun cancel(context: Context, medication: MedicationEntity) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        for (index in 0 until MAX_REMINDER_SLOTS) {
            val pendingIntent = buildPendingIntent(context, medication, index, 0, 0)
            alarmManager.cancel(pendingIntent)
        }
    }

    private fun areMedicationRemindersAllowed(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val masterEnabled = prefs.getBoolean(KEY_REMINDERS_ENABLED, true)
        val medEnabled = prefs.getBoolean(KEY_MED_REMINDERS_ENABLED, true)
        return masterEnabled && medEnabled
    }

    private fun resolveReminderTimes(context: Context, medication: MedicationEntity): List<Pair<Int, Int>> {
        val parsed = parseScheduledTimes(medication.scheduledTimes)
        if (parsed.isNotEmpty()) return parsed.take(MAX_REMINDER_SLOTS)

        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val defaultHour = prefs.getInt(KEY_DEFAULT_REMINDER_HOUR, 8)
        val defaultMinute = prefs.getInt(KEY_DEFAULT_REMINDER_MINUTE, 0)
        return listOf(defaultHour to defaultMinute)
    }

    private fun parseScheduledTimes(raw: String?): List<Pair<Int, Int>> {
        if (raw.isNullOrBlank()) return emptyList()

        return raw
            .split(",", ";", "|")
            .mapNotNull { token ->
                val cleaned = token.trim()
                if (cleaned.isBlank()) return@mapNotNull null

                val parts = cleaned.split(":")
                if (parts.size != 2) return@mapNotNull null

                val hour = parts[0].trim().toIntOrNull() ?: return@mapNotNull null
                val minute = parts[1].trim().toIntOrNull() ?: return@mapNotNull null
                if (hour !in 0..23 || minute !in 0..59) return@mapNotNull null

                hour to minute
            }
            .distinct()
            .sortedWith(compareBy<Pair<Int, Int>> { it.first }.thenBy { it.second })
    }

    private fun resolveTriggerAtMillis(hour: Int, minute: Int): Long {
        val cal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        if (cal.timeInMillis <= System.currentTimeMillis()) {
            cal.add(Calendar.DAY_OF_YEAR, 1)
        }

        return cal.timeInMillis
    }

    private fun buildPendingIntent(
        context: Context,
        medication: MedicationEntity,
        slotIndex: Int,
        hour: Int,
        minute: Int
    ): PendingIntent {
        val intent = Intent(context, MedicationReminderReceiver::class.java).apply {
            action = "com.caretracker.MEDICATION_REMINDER_$slotIndex"
            putExtra("med_id", medication.id)
            putExtra("med_name", medication.name)
            putExtra("med_dosage", medication.dosage)
            putExtra("slot_index", slotIndex)
            putExtra("slot_hour", hour)
            putExtra("slot_minute", minute)
        }

        val requestCode = REQUEST_CODE_BASE + (medication.id.toInt() * MAX_REMINDER_SLOTS) + slotIndex

        return PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}
