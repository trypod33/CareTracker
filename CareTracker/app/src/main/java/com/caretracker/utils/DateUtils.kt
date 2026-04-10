package com.caretracker.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtils {
    fun formatDate(timestamp: Long): String = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(timestamp))
    fun formatDateTime(timestamp: Long): String = SimpleDateFormat("MMM dd, yyyy h:mm a", Locale.getDefault()).format(Date(timestamp))
    fun todayKey(): String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
}
