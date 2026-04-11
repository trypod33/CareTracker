package com.caretracker.ui.health

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.SeekBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.caretracker.data.models.DailyHealthEntry
import com.caretracker.databinding.ActivityHealthLogBinding
import com.caretracker.utils.SessionManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class HealthLogActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHealthLogBinding
    private val viewModel: HealthViewModel by viewModels()

    @Inject lateinit var sessionManager: SessionManager

    private var entryId: Long = 0L
    private val dateFmt   = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    private val timeFmt12 = SimpleDateFormat("hh:mm a",   Locale.getDefault())
    private val dateKey   = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    // Store selected timestamps so we can save them
    private var bpTimestamp: Long? = null
    private var bsTimestamp: Long? = null
    // Date for the entry (defaults to today)
    private var entryDateCal: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHealthLogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        entryId = intent.getLongExtra("ENTRY_ID", 0L)
        supportActionBar?.title = if (entryId != 0L) "Edit Health Data" else "Update Health Data"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Default date = today
        binding.etDate.setText(dateFmt.format(entryDateCal.time))

        setupDatePicker()
        setupTimePickers()
        setupSeekBars()

        if (entryId != 0L) loadEntry(entryId)

        binding.btnSaveHealthLog.setOnClickListener { saveEntry() }
        binding.btnCancel.setOnClickListener { finish() }
    }

    override fun onSupportNavigateUp(): Boolean { finish(); return true }

    // ─── Date picker ───────────────────────────────────────────────────────────
    private fun setupDatePicker() {
        binding.etDate.setOnClickListener {
            DatePickerDialog(
                this,
                { _, y, m, d ->
                    entryDateCal.set(y, m, d)
                    binding.etDate.setText(dateFmt.format(entryDateCal.time))
                },
                entryDateCal.get(Calendar.YEAR),
                entryDateCal.get(Calendar.MONTH),
                entryDateCal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    // ─── Time pickers ──────────────────────────────────────────────────────────
    private fun setupTimePickers() {
        val now = Calendar.getInstance()
        binding.etBpTime.setOnClickListener {
            TimePickerDialog(this, { _, h, m ->
                val cal = Calendar.getInstance().apply { set(Calendar.HOUR_OF_DAY, h); set(Calendar.MINUTE, m) }
                bpTimestamp = cal.timeInMillis
                binding.etBpTime.setText(timeFmt12.format(cal.time))
            }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), false).show()
        }
        binding.etBsTime.setOnClickListener {
            TimePickerDialog(this, { _, h, m ->
                val cal = Calendar.getInstance().apply { set(Calendar.HOUR_OF_DAY, h); set(Calendar.MINUTE, m) }
                bsTimestamp = cal.timeInMillis
                binding.etBsTime.setText(timeFmt12.format(cal.time))
            }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), false).show()
        }
    }

    // ─── SeekBars ──────────────────────────────────────────────────────────────
    private fun setupSeekBars() {
        binding.seekSleepQuality.max = 9   // 1-10, stored as index+1
        binding.seekMood.max        = 9
        binding.seekEnergy.max      = 9

        fun SeekBar.labelWith(tv: android.widget.TextView) {
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(sb: SeekBar, p: Int, user: Boolean) {
                    tv.text = if (p == 0) "None" else "${p + 1}"
                }
                override fun onStartTrackingTouch(sb: SeekBar) {}
                override fun onStopTrackingTouch(sb: SeekBar) {}
            })
            // Trigger label for initial value
            tv.text = if (progress == 0) "None" else "${progress + 1}"
        }

        binding.seekSleepQuality.labelWith(binding.tvSleepQualityVal)
        binding.seekMood.labelWith(binding.tvMoodVal)
        binding.seekEnergy.labelWith(binding.tvEnergyVal)
    }

    // ─── Load existing entry ───────────────────────────────────────────────────
    private fun loadEntry(id: Long) {
        lifecycleScope.launch {
            val entry = viewModel.getById(id) ?: return@launch
            // Date
            runCatching {
                entryDateCal.time = dateKey.parse(entry.date)!!
                binding.etDate.setText(dateFmt.format(entryDateCal.time))
            }
            // Body
            binding.etWeight.setText(entry.weightValue?.toString() ?: "")
            binding.spinnerWeightUnit.setSelection(
                if (entry.weightUnit == "kg") 1 else 0
            )
            binding.etHeartRate.setText(entry.heartRate?.toString() ?: "")
            // Vitals
            binding.etBpSystolic.setText(entry.bpSystolic?.toString()  ?: "")
            binding.etBpDiastolic.setText(entry.bpDiastolic?.toString() ?: "")
            binding.etBloodSugar.setText(entry.bloodSugar?.toString()   ?: "")
            entry.bpTimestamp?.let {
                bpTimestamp = it
                binding.etBpTime.setText(timeFmt12.format(Date(it)))
            }
            entry.bsTimestamp?.let {
                bsTimestamp = it
                binding.etBsTime.setText(timeFmt12.format(Date(it)))
            }
            // Sleep
            binding.etSleepHours.setText(entry.sleepHours?.toString() ?: "")
            entry.sleepQuality?.let {
                binding.seekSleepQuality.progress = (it - 1).coerceIn(0, 9)
            }
            // Wellness
            entry.mood?.let        { binding.seekMood.progress   = (it - 1).coerceIn(0, 9) }
            entry.energyLevel?.let { binding.seekEnergy.progress = (it - 1).coerceIn(0, 9) }
            // Activity
            binding.etSteps.setText(entry.steps?.toString()           ?: "")
            binding.etExercise.setText(entry.exerciseMinutes?.toString() ?: "")
            binding.etWater.setText(entry.waterOz?.toString()          ?: "")
            binding.etCalories.setText(entry.calories?.toString()       ?: "")
            // Notes
            binding.etNotes.setText(entry.notes ?: "")
        }
    }

    // ─── Save ──────────────────────────────────────────────────────────────────
    private fun saveEntry() {
        val pid = sessionManager.activePersonId
        if (pid == -1L) { finish(); return }

        fun Int.zeroToNull() = if (this == 0) null else this

        val sqRaw = binding.seekSleepQuality.progress + 1
        val moodRaw   = binding.seekMood.progress   + 1
        val energyRaw = binding.seekEnergy.progress + 1

        val entry = DailyHealthEntry(
            id       = entryId,
            personId = pid,
            date     = dateKey.format(entryDateCal.time),

            weightValue  = binding.etWeight.text.toString().toFloatOrNull(),
            weightUnit   = binding.spinnerWeightUnit.selectedItem.toString(),
            heartRate    = binding.etHeartRate.text.toString().toIntOrNull(),

            bpSystolic   = binding.etBpSystolic.text.toString().toIntOrNull(),
            bpDiastolic  = binding.etBpDiastolic.text.toString().toIntOrNull(),
            bloodSugar   = binding.etBloodSugar.text.toString().toFloatOrNull(),
            bpTimestamp  = bpTimestamp,
            bsTimestamp  = bsTimestamp,

            sleepHours   = binding.etSleepHours.text.toString().toFloatOrNull(),
            sleepQuality = if (binding.seekSleepQuality.progress == 0 && binding.tvSleepQualityVal.text == "None") null else sqRaw,

            mood         = if (binding.seekMood.progress   == 0 && binding.tvMoodVal.text   == "None") null else moodRaw,
            energyLevel  = if (binding.seekEnergy.progress == 0 && binding.tvEnergyVal.text == "None") null else energyRaw,

            steps            = binding.etSteps.text.toString().toIntOrNull(),
            exerciseMinutes  = binding.etExercise.text.toString().toIntOrNull(),
            waterOz          = binding.etWater.text.toString().toFloatOrNull(),
            calories         = binding.etCalories.text.toString().toIntOrNull(),

            notes = binding.etNotes.text.toString().trim().ifEmpty { null }
        )
        viewModel.save(entry)
        finish()
    }
}
