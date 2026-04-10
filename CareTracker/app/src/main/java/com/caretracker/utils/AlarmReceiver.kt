package com.caretracker.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        NotificationHelper.showSimpleNotification(
            context,
            "CareTracker Reminder",
            intent?.getStringExtra("message") ?: "You have a care reminder."
        )
    }
}
