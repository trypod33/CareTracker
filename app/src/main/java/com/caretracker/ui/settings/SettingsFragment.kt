package com.caretracker.ui.settings
import com.caretracker.data.entities.TaskEntity

import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.caretracker.CareTrackerApp
import com.caretracker.R
import com.caretracker.notifications.TaskReminderScheduler
import com.caretracker.ui.SettingsActionListener
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlinx.coroutines.launch

class SettingsFragment : Fragment() {

    companion object {
        private const val PREFS_NAME = "caretracker"
        private const val KEY_REMINDERS_ENABLED = "reminders_enabled"
        private const val KEY_DEFAULT_REMINDER_HOUR = "default_reminder_hour"
        private const val KEY_DEFAULT_REMINDER_MINUTE = "default_reminder_minute"
        private const val KEY_TASK_REMINDERS_ENABLED = "task_reminders_enabled"
        private const val KEY_MED_REMINDERS_ENABLED = "med_reminders_enabled"
    }

    private var listener: SettingsActionListener? = null

    private lateinit var tvCurrentPersonValue: TextView
    private lateinit var tvReminderStatusValue: TextView
    private lateinit var tvPrivacyStatusValue: TextView
    private lateinit var tvBackupStatusValue: TextView
    private lateinit var tvRoadmapStatus: TextView
    private lateinit var tvDefaultReminderTime: TextView

    private lateinit var switchRemindersMaster: Switch
    private lateinit var switchTaskReminders: Switch
    private lateinit var switchMedicationReminders: Switch

    private lateinit var rowDefaultReminderTime: View
    private lateinit var rowSignOut: View
    private lateinit var rowDeletePerson: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listener = activity as? SettingsActionListener

        bindViews(view)
        bindStaticText()
        bindClicks(view)
        loadReminderSettings()
    }

    private fun bindViews(view: View) {
        tvCurrentPersonValue = view.findViewById(R.id.tvCurrentPersonValue)
        tvReminderStatusValue = view.findViewById(R.id.tvReminderStatusValue)
        tvPrivacyStatusValue = view.findViewById(R.id.tvPrivacyStatusValue)
        tvBackupStatusValue = view.findViewById(R.id.tvBackupStatusValue)
        tvRoadmapStatus = view.findViewById(R.id.tvRoadmapStatus)
        tvDefaultReminderTime = view.findViewById(R.id.tvDefaultReminderTime)

        switchRemindersMaster = view.findViewById(R.id.switchRemindersMaster)
        switchTaskReminders = view.findViewById(R.id.switchTaskReminders)
        switchMedicationReminders = view.findViewById(R.id.switchMedicationReminders)

        rowDefaultReminderTime = view.findViewById(R.id.row_default_reminder_time)
        rowSignOut = view.findViewById(R.id.row_sign_out)
        rowDeletePerson = view.findViewById(R.id.row_delete_person)
    }

    private fun bindStaticText() {
        tvCurrentPersonValue.text = "Current person: managed from top selector"
        tvPrivacyStatusValue.text = "Privacy policy and disclaimer needed before store release."
        tvBackupStatusValue.text = "Tap below to export or restore your data."
        tvRoadmapStatus.text = "Milestone: v0.9.0 reminder settings"
    }

    private fun bindClicks(view: View) {
        click(view, R.id.row_manage_people, "Manage People coming soon")
        click(view, R.id.row_privacy_policy, "Privacy policy placeholder")
        click(view, R.id.row_medical_disclaimer, "Medical disclaimer placeholder")
        click(view, R.id.row_app_lock, "App lock coming soon")
        click(view, R.id.row_backup_restore, "Backup and restore coming soon")
        click(view, R.id.row_about, "CareTracker v0.9.0-dev")

        rowSignOut.setOnClickListener {
            listener?.onSignOutRequested()
                ?: Toast.makeText(requireContext(), "Sign out unavailable", Toast.LENGTH_SHORT).show()
        }

        rowDeletePerson.setOnClickListener {
            listener?.onDeleteCurrentPersonRequested()
                ?: Toast.makeText(requireContext(), "Delete unavailable", Toast.LENGTH_SHORT).show()
        }

        switchRemindersMaster.setOnCheckedChangeListener { _, isChecked ->
            prefs().edit().putBoolean(KEY_REMINDERS_ENABLED, isChecked).apply()
            updateReminderUiState(isChecked)
            updateReminderStatusText()
            rescheduleAllTaskReminders()
        }

        switchTaskReminders.setOnCheckedChangeListener { _, isChecked ->
            prefs().edit().putBoolean(KEY_TASK_REMINDERS_ENABLED, isChecked).apply()
            updateReminderStatusText()
            rescheduleAllTaskReminders()
        }

        switchMedicationReminders.setOnCheckedChangeListener { _, isChecked ->
            prefs().edit().putBoolean(KEY_MED_REMINDERS_ENABLED, isChecked).apply()
            updateReminderStatusText()
        }

        rowDefaultReminderTime.setOnClickListener {
            if (!switchRemindersMaster.isChecked) return@setOnClickListener
            showTimePicker()
        }
    }

    private fun loadReminderSettings() {
        val prefs = prefs()
        val remindersEnabled = prefs.getBoolean(KEY_REMINDERS_ENABLED, true)
        val defaultHour = prefs.getInt(KEY_DEFAULT_REMINDER_HOUR, 8)
        val defaultMinute = prefs.getInt(KEY_DEFAULT_REMINDER_MINUTE, 0)
        val taskRemindersEnabled = prefs.getBoolean(KEY_TASK_REMINDERS_ENABLED, true)
        val medRemindersEnabled = prefs.getBoolean(KEY_MED_REMINDERS_ENABLED, true)

        switchRemindersMaster.isChecked = remindersEnabled
        switchTaskReminders.isChecked = taskRemindersEnabled
        switchMedicationReminders.isChecked = medRemindersEnabled
        tvDefaultReminderTime.text = formatTime(defaultHour, defaultMinute)

        updateReminderUiState(remindersEnabled)
        updateReminderStatusText()
    }

    private fun updateReminderUiState(enabled: Boolean) {
        rowDefaultReminderTime.isEnabled = enabled
        rowDefaultReminderTime.alpha = if (enabled) 1f else 0.5f
        switchTaskReminders.isEnabled = enabled
        switchMedicationReminders.isEnabled = enabled
    }

    private fun updateReminderStatusText() {
        val master = switchRemindersMaster.isChecked
        val tasks = switchTaskReminders.isChecked
        val meds = switchMedicationReminders.isChecked

        tvReminderStatusValue.text = when {
            !master -> "All reminders are turned off."
            tasks && meds -> "Task and medication reminders are on."
            tasks -> "Task reminders are on. Medication reminders are off."
            meds -> "Medication reminders are on. Task reminders are off."
            else -> "Reminders are enabled, but no reminder types are selected."
        }
    }

    private fun showTimePicker() {
        val prefs = prefs()
        val hour = prefs.getInt(KEY_DEFAULT_REMINDER_HOUR, 8)
        val minute = prefs.getInt(KEY_DEFAULT_REMINDER_MINUTE, 0)

        TimePickerDialog(requireContext(), { _, selectedHour, selectedMinute ->
            prefs.edit()
                .putInt(KEY_DEFAULT_REMINDER_HOUR, selectedHour)
                .putInt(KEY_DEFAULT_REMINDER_MINUTE, selectedMinute)
                .apply()

            tvDefaultReminderTime.text = formatTime(selectedHour, selectedMinute)
            Toast.makeText(requireContext(), "Default reminder time updated", Toast.LENGTH_SHORT).show()
            rescheduleAllTaskReminders()
        }, hour, minute, false).show()
    }

    private fun rescheduleAllTaskReminders() {
        val app = requireActivity().application as CareTrackerApp
        val context = requireContext().applicationContext

        viewLifecycleOwner.lifecycleScope.launch {
            val allTasks = app.repository.getAllTasksForUserOnce(app.currentUserId)
            allTasks.forEach { task: TaskEntity ->
                TaskReminderScheduler.cancel(context, task)
                TaskReminderScheduler.schedule(context, task)
            }
        }
    }

    private fun formatTime(hour: Int, minute: Int): String {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, hour)
        cal.set(Calendar.MINUTE, minute)
        return SimpleDateFormat("h:mm a", Locale.getDefault()).format(cal.time)
    }

    private fun prefs() = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private fun click(root: View, id: Int, message: String) {
        root.findViewById<View>(id).setOnClickListener {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }
}
