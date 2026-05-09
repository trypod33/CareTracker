package com.caretracker.notifications

import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.caretracker.CareTrackerApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReminderRestoreReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action ?: return

        val shouldHandle = action == Intent.ACTION_BOOT_COMPLETED ||
            action == Intent.ACTION_MY_PACKAGE_REPLACED ||
            action == AlarmManager.ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED

        if (!shouldHandle) return

        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                if (action == AlarmManager.ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED &&
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
                ) {
                    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    if (!alarmManager.canScheduleExactAlarms()) {
                        return@launch
                    }
                }

                val app = context.applicationContext as CareTrackerApp
                val users = app.repository.getAllUsersOnce().filter { it.isActive }

                users.forEach { user ->
                    val meds = app.repository.getMedicationsForUserOnce(user.id)
                    meds.filter { it.isActive }.forEach { med ->
                        MedicationReminderScheduler.cancel(context, med)
                        MedicationReminderScheduler.schedule(context, med)
                    }
                }
            } finally {
                pendingResult.finish()
            }
        }
    }
}
