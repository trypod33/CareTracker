package com.caretracker.ui.backup

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.caretracker.CareTrackerApp
import com.caretracker.R
import com.caretracker.data.backup.BackupManager
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class BackupFragment : Fragment() {

    private lateinit var tvLastBackup: TextView
    private lateinit var tvStatus: TextView
    private lateinit var btnExport: Button
    private lateinit var btnRestore: Button

    private val pickFile = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { restoreFromUri(it) }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_backup, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvLastBackup = view.findViewById(R.id.tvLastBackup)
        tvStatus     = view.findViewById(R.id.tvBackupStatus)
        btnExport    = view.findViewById(R.id.btnExportBackup)
        btnRestore   = view.findViewById(R.id.btnRestoreBackup)
        refreshLastBackup()
        btnExport.setOnClickListener  { doExport() }
        btnRestore.setOnClickListener { pickRestoreFile() }
    }

    private fun doExport() {
        val app = requireActivity().application as CareTrackerApp
        btnExport.isEnabled = false
        tvStatus.text = "Exporting..."
        viewLifecycleOwner.lifecycleScope.launch {
            BackupManager.exportBackup(requireContext(), app.repository, app.currentUserId)
                .onSuccess { file ->
                    prefs().edit().putLong("last_backup_at", System.currentTimeMillis()).apply()
                    tvStatus.text = "Saved to Downloads: " + file.name
                    refreshLastBackup()
                }
                .onFailure { tvStatus.text = "Export failed: " + it.message }
            btnExport.isEnabled = true
        }
    }

    private fun pickRestoreFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "*/*"
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        pickFile.launch(Intent.createChooser(intent, "Select CareTracker backup"))
    }

    private fun restoreFromUri(uri: Uri) {
        val app = requireActivity().application as CareTrackerApp
        btnRestore.isEnabled = false
        tvStatus.text = "Restoring..."
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val stream = requireContext().contentResolver.openInputStream(uri)
                if (stream == null) {
                    tvStatus.text = "Cannot open file"
                    btnRestore.isEnabled = true
                    return@launch
                }
                val tmp = File(requireContext().cacheDir, "ct_restore.json")
                tmp.outputStream().use { stream.copyTo(it) }
                BackupManager.restoreBackup(tmp, app.repository)
                    .onSuccess { summary ->
                        tvStatus.text = "Restore complete: " + summary
                        Toast.makeText(requireContext(), "Restore complete!", Toast.LENGTH_SHORT).show()
                    }
                    .onFailure { tvStatus.text = "Restore failed: " + it.message }
                tmp.delete()
            } catch (e: Exception) {
                tvStatus.text = "Error: " + e.message
            }
            btnRestore.isEnabled = true
        }
    }

    private fun refreshLastBackup() {
        val at = prefs().getLong("last_backup_at", 0L)
        tvLastBackup.text = if (at == 0L) "No backup created yet"
        else "Last backup: " + SimpleDateFormat("MMM d, yyyy h:mm a", Locale.getDefault()).format(Date(at))
    }

    private fun prefs() = requireContext().getSharedPreferences("caretracker_prefs", 0)
}
