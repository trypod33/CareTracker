package com.caretracker.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.caretracker.R
import com.caretracker.ui.SettingsActionListener

class SettingsFragment : Fragment() {

    private var listener: SettingsActionListener? = null

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

        setText(view, R.id.tvCurrentPersonValue, "Current person: managed from top selector")
        setText(view, R.id.tvReminderStatusValue, "Task reminders available. More controls coming soon.")
        setText(view, R.id.tvPrivacyStatusValue, "Privacy policy and disclaimer needed before store release.")
        setText(view, R.id.tvBackupStatusValue, "Backup and restore not built yet.")
        setText(view, R.id.tvRoadmapStatus, "Milestone: v0.8.1 - Settings actions wired")

        click(view, R.id.row_manage_people, "Manage People coming soon")
        click(view, R.id.row_reminder_settings, "Reminder settings coming soon")
        click(view, R.id.row_privacy_policy, "Privacy policy placeholder")
        click(view, R.id.row_medical_disclaimer, "Medical disclaimer placeholder")
        click(view, R.id.row_app_lock, "App lock coming soon")
        click(view, R.id.row_backup_restore, "Backup and restore coming soon")
        click(view, R.id.row_about, "CareTracker v0.8.1-dev")

        view.findViewById<View>(R.id.row_sign_out).setOnClickListener {
            listener?.onSignOutRequested()
                ?: Toast.makeText(requireContext(), "Sign out unavailable", Toast.LENGTH_SHORT).show()
        }

        view.findViewById<View>(R.id.row_delete_person).setOnClickListener {
            listener?.onDeleteCurrentPersonRequested()
                ?: Toast.makeText(requireContext(), "Delete unavailable", Toast.LENGTH_SHORT).show()
        }
    }

    private fun click(root: View, id: Int, message: String) {
        root.findViewById<View>(id).setOnClickListener {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setText(root: View, id: Int, value: String) {
        root.findViewById<TextView>(id).text = value
    }
}
