package com.caretracker.ui.health

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.caretracker.data.models.HealthLog
import com.caretracker.databinding.ActivityHealthLogBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HealthLogActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHealthLogBinding
    private val viewModel: HealthViewModel by viewModels()
    private var logId: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHealthLogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        logId = intent.getLongExtra("LOG_ID", 0L)
        supportActionBar?.title = if (logId != 0L) "Edit Health Log" else "Log Health Data"

        setupDropdowns()

        if (logId != 0L) loadLogData()

        binding.btnSaveHealthLog.setOnClickListener { saveLog() }
    }

    private fun setupDropdowns() {
        val metrics = arrayOf(
            "Blood Pressure", "Blood Sugar", "Weight", "Temperature",
            "Heart Rate", "Oxygen Level", "Mood", "Sleep Hours",
            "Pain Level", "Steps", "Water Intake", "Other"
        )
        binding.etMetricType.setAdapter(
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, metrics)
        )
    }

    private fun loadLogData() {
        lifecycleScope.launch {
            viewModel.healthLogs.collect { logs ->
                val log = logs.find { it.id == logId }
                log?.let {
                    binding.etMetricType.setText(it.metricId.toString(), false)
                    binding.etValue.setText(it.value)
                    binding.etNotes.setText(it.notes ?: "")
                }
            }
        }
    }

    private fun saveLog() {
        val metricType = binding.etMetricType.text.toString().trim()
        val value = binding.etValue.text.toString().trim()

        if (metricType.isEmpty()) {
            binding.etMetricType.error = "Please select a metric type"
            return
        }
        if (value.isEmpty()) {
            binding.etValue.error = "Value is required"
            return
        }

        // Use metricId=0 as a general log; metric linking can be added later
        val log = HealthLog(
            id = logId,
            personId = 1L,
            metricId = 0L,
            value = "$metricType: $value",
            notes = binding.etNotes.text.toString().trim().ifEmpty { null },
            timestamp = if (logId == 0L) System.currentTimeMillis() else System.currentTimeMillis()
        )

        viewModel.saveLog(log)
        finish()
    }
}
