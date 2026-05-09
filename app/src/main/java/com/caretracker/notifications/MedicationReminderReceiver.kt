package com.caretracker.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.caretracker.CareTrackerApp
import com.caretracker.R
import com.caretracker.ui.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MedicationReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val medId = intent.getLongExtra("med_id", -1L)
        val medName = intent.getStringExtra("med_name") ?: "Medication reminder"
        val dosage = intent.getStringExtra("med_dosage")
        val slotHour = intent.getIntExtra("slot_hour", -1)
        val slotMinute = intent.getIntExtra("slot_minute", -1)
        val slotIndex = intent.getIntExtra("slot_index", 0)

        createChannel(context)

        val openIntent = Intent(context, MainActivity::class.java).apply {
            putExtra("open_meds", true)
            putExtra("med_id", medId)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val openPendingIntent = PendingIntent.getActivity(
            context,
            10000 + medId.toInt() + slotIndex,
            openIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val timeText = if (slotHour in 0..23 && slotMinute in 0..59) {
            formatTime(slotHour, slotMinute)
        } else null

        val text = when {
            !dosage.isNullOrBlank() && !timeText.isNullOrBlank() -> "Dose: $dosage at $timeText"
            !dosage.isNullOrBlank() -> "Dose: $dosage"
            !timeText.isNullOrBlank() -> "Scheduled for $timeText"
            else -> "Time to take your medication"
        }

        val notification = NotificationCompat.Builder(context, "medication_reminders")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(medName)
            .setContentText(text)
            .setStyle(NotificationCompat.BigTextStyle().bigText(text))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(openPendingIntent)
            .build()

        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.notify(10000 + medId.toInt() + slotIndex, notification)

        if (medId > 0L) {
            val pendingResult = goAsync()
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val app = context.applicationContext as CareTrackerApp
                    val med = app.repository.getMedicationById(medId)
                    if (med != null && med.isActive) {
                        MedicationReminderScheduler.schedule(context.applicationContext, med)
                    }
                } finally {
                    pendingResult.finish()
                }
            }
        }
    }

    private fun formatTime(hour: Int, minute: Int): String {
        val cal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return SimpleDateFormat("hh:mm a", Locale.getDefault()).format(cal.time)
    }

    private fun createChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "medication_reminders",
                "Medication reminders",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Reminders for medications"
            }

            val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nm.createNotificationChannel(channel)
        }
    }
}
