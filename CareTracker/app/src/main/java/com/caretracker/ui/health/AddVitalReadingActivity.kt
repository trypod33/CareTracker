package com.caretracker.ui.health

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.caretracker.data.models.VitalReading
import com.caretracker.databinding.ActivityAddVitalReadingBinding
import com.caretracker.utils.SessionManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class AddVitalReadingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddVitalReadingBinding
    private val viewModel: VitalReadingViewModel by viewModels()

    @Inject lateinit var sessionManager: SessionManager

    private val dateFmt   = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    private val timeFmt12 = SimpleDateFormat("hh:mm a",   Locale.getDefault())
    private val dateKey   = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    private var selectedCal: Calendar = Calendar.getInstance()
    private var editingId: Long = 0L

    private val TYPE_BP      = "BLOOD_PRESSURE"
    private val TYPE_BS      = "BLOOD_SUGAR"
    private val TYPE_HR      = "HEART_RATE"
    private val TYPE_WEIGHT  = "WEIGHT"
    private val TYPE_O2      = "OXYGEN_SAT"
    private val TYPE_TEMP    = "TEMPERATURE"

    private val readingTypeLabels = listOf(
        "Blood Pressure", "Blood Sugar", "Heart Rate",
        "Weight", "Oxygen Saturation", "Temperature"
    )
    private val readingTypeValues = listOf(TYPE_BP, TYPE_BS, TYPE_HR, TYPE_WEIGHT, TYPE_O2, TYPE_TEMP)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddVitalReadingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        editingId = intent.getLongExtra("READING_ID", 0L)
        val presetType = intent.getStringExtra("READING_TYPE")

        supportActionBar?.title = if (editingId != 0L) "Edit Vital Reading" else "Add Vital Reading"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.etDate.setText(dateFmt.format(selectedCal.time))
        binding.etTime.setText(timeFmt12.format(selectedCal.time))

        setupDatePicker()
        setupTimePicker()
        setupReadingTypeDropdown(presetType)
        setupSubDropdowns()

        if (editingId != 0L) loadExisting(editingId)

        binding.btnSave.setOnClickListener { saveReading() }
        binding.btnCancel.setOnClickListener { finish() }
    }

    override fun onSupportNavigateUp(): Boolean { finish(); return true }

    // ─── Date picker ───────────────────────────────────────────────────────────
    private fun setupDatePicker() {
        binding.etDate.setOnClickListener {
            DatePickerDialog(
                this,
                { _, y, m, d ->
                    selectedCal.set(y, m, d)
                    binding.etDate.setText(dateFmt.format(selectedCal.time))
                },
                selectedCal.get(Calendar.YEAR),
                selectedCal.get(Calendar.MONTH),
                selectedCal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    // ─── Time picker ───────────────────────────────────────────────────────────
    private fun setupTimePicker() {
        binding.etTime.setOnClickListener {
            TimePickerDialog(
                this,
                { _, h, m ->
                    selectedCal.set(Calendar.HOUR_OF_DAY, h)
                    selectedCal.set(Calendar.MINUTE, m)
                    selectedCal.set(Calendar.SECOND, 0)
                    binding.etTime.setText(timeFmt12.format(selectedCal.time))
                },
                selectedCal.get(Calendar.HOUR_OF_DAY),
                selectedCal.get(Calendar.MINUTE),
                false
            ).show()
        }
    }

    // ─── Reading type dropdown ─────────────────────────────────────────────────
    private fun setupReadingTypeDropdown(presetType: String?) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, readingTypeLabels)
        binding.spinnerReadingType.setAdapter(adapter)

        binding.spinnerReadingType.setOnItemClickListener { _, _, pos, _ ->
            showSectionForType(readingTypeValues[pos])
        }

        if (presetType != null) {
            val idx = readingTypeValues.indexOf(presetType)
            if (idx >= 0) {
                binding.spinnerReadingType.setText(readingTypeLabels[idx], false)
                showSectionForType(presetType)
            }
        }
    }

    // ─── Show/hide metric-specific sections ────────────────────────────────────
    private fun showSectionForType(type: String) {
        binding.sectionBP.visibility     = if (type == TYPE_BP)     View.VISIBLE else View.GONE
        binding.sectionBS.visibility     = if (type == TYPE_BS)     View.VISIBLE else View.GONE
        binding.sectionHR.visibility     = if (type == TYPE_HR)     View.VISIBLE else View.GONE
        binding.sectionWeight.visibility = if (type == TYPE_WEIGHT) View.VISIBLE else View.GONE
        binding.sectionOxygen.visibility = if (type == TYPE_O2)     View.VISIBLE else View.GONE
        binding.sectionTemp.visibility   = if (type == TYPE_TEMP)   View.VISIBLE else View.GONE
    }

    // ─── Sub-dropdowns ─────────────────────────────────────────────────────────
    private fun setupSubDropdowns() {
        binding.spinnerBpPosition.setAdapter(ArrayAdapter(this,
            android.R.layout.simple_dropdown_item_1line,
            listOf("Sitting", "Standing", "Lying Down")))

        binding.spinnerBsContext.setAdapter(ArrayAdapter(this,
            android.R.layout.simple_dropdown_item_1line,
            listOf("Fasting", "Before Meal", "After Meal", "Bedtime", "Random")))

        binding.spinnerHrContext.setAdapter(ArrayAdapter(this,
            android.R.layout.simple_dropdown_item_1line,
            listOf("Resting", "After Exercise", "During Activity")))

        binding.spinnerWeightUnit.setAdapter(ArrayAdapter(this,
            android.R.layout.simple_dropdown_item_1line, listOf("lbs", "kg")))
        binding.spinnerWeightUnit.setText("lbs", false)

        binding.spinnerTempUnit.setAdapter(ArrayAdapter(this,
            android.R.layout.simple_dropdown_item_1line, listOf("F", "C")))
        binding.spinnerTempUnit.setText("F", false)
    }

    // ─── Load existing reading (edit mode) ────────────────────────────────────
    private fun loadExisting(id: Long) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                val r = viewModel.getById(id) ?: return@repeatOnLifecycle

                runCatching {
                    val cal = Calendar.getInstance().apply { timeInMillis = r.readingTimestamp }
                    selectedCal = cal
                    binding.etDate.setText(dateFmt.format(cal.time))
                    binding.etTime.setText(timeFmt12.format(cal.time))
                }

                val idx = readingTypeValues.indexOf(r.readingType)
                if (idx >= 0) {
                    binding.spinnerReadingType.setText(readingTypeLabels[idx], false)
                    showSectionForType(r.readingType)
                }

                r.bpSystolic?.let  { binding.etBpSystolic.setText(it.toString()) }
                r.bpDiastolic?.let { binding.etBpDiastolic.setText(it.toString()) }
                r.bpPosition?.let  { binding.spinnerBpPosition.setText(it, false) }
                r.bloodSugar?.let  { binding.etBloodSugar.setText(it.toString()) }
                r.bsContext?.let   { binding.spinnerBsContext.setText(it, false) }
                r.heartRate?.let   { binding.etHeartRate.setText(it.toString()) }
                r.hrContext?.let   { binding.spinnerHrContext.setText(it, false) }
                r.weightValue?.let { binding.etWeight.setText(it.toString()) }
                r.weightUnit?.let  { binding.spinnerWeightUnit.setText(it, false) }
                r.oxygenSat?.let   { binding.etOxygenSat.setText(it.toString()) }
                r.temperature?.let { binding.etTemperature.setText(it.toString()) }
                r.tempUnit?.let    { binding.spinnerTempUnit.setText(it, false) }
                r.notes?.let       { binding.etNotes.setText(it) }
            }
        }
    }

    // ─── Save ──────────────────────────────────────────────────────────────────
    private fun saveReading() {
        val pid = sessionManager.activePersonId
        if (pid == -1L) { finish(); return }

        val selectedType = readingTypeValues.getOrNull(
            readingTypeLabels.indexOf(binding.spinnerReadingType.text.toString())
        ) ?: run {
            binding.tilReadingType.error = "Please select a reading type"
            return
        }
        binding.tilReadingType.error = null

        val reading = VitalReading(
            id               = editingId,
            personId         = pid,
            readingDate      = dateKey.format(selectedCal.time),
            readingTimestamp = selectedCal.timeInMillis,
            readingType      = selectedType,

            bpSystolic  = binding.etBpSystolic.text.toString().toIntOrNull(),
            bpDiastolic = binding.etBpDiastolic.text.toString().toIntOrNull(),
            bpPosition  = binding.spinnerBpPosition.text.toString().ifEmpty { null },

            bloodSugar  = binding.etBloodSugar.text.toString().toFloatOrNull(),
            bsContext   = binding.spinnerBsContext.text.toString().ifEmpty { null },

            heartRate   = binding.etHeartRate.text.toString().toIntOrNull(),
            hrContext   = binding.spinnerHrContext.text.toString().ifEmpty { null },

            weightValue = binding.etWeight.text.toString().toFloatOrNull(),
            weightUnit  = binding.spinnerWeightUnit.text.toString().ifEmpty { "lbs" },

            oxygenSat   = binding.etOxygenSat.text.toString().toIntOrNull(),

            temperature = binding.etTemperature.text.toString().toFloatOrNull(),
            tempUnit    = binding.spinnerTempUnit.text.toString().ifEmpty { "F" },

            notes = binding.etNotes.text.toString().trim().ifEmpty { null }
        )
        viewModel.save(reading)
        finish()
    }
}
