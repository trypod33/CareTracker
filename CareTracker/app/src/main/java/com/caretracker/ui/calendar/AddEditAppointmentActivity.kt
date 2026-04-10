package com.caretracker.ui.calendar

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.caretracker.data.models.Appointment
import com.caretracker.databinding.ActivityAddEditAppointmentBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class AddEditAppointmentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditAppointmentBinding
    private val viewModel: AppointmentViewModel by viewModels()
    private var appointmentId: Long = 0L
    private var selectedDateTimeMs: Long = System.currentTimeMillis()
    private val displayFormat = SimpleDateFormat("MMM dd, yyyy  h:mm a", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditAppointmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appointmentId = intent.getLongExtra("APPOINTMENT_ID", 0L)
        supportActionBar?.title = if (appointmentId != 0L) "Edit Appointment" else "Add Appointment"

        setupDropdowns()
        updateDateTimeDisplay()

        binding.etDateTime.setOnClickListener { showDatePicker() }

        if (appointmentId != 0L) loadAppointmentData()

        binding.btnSaveAppointment.setOnClickListener { saveAppointment() }
    }

    private fun setupDropdowns() {
        val types = arrayOf("general", "doctor", "therapy", "lab", "dental", "specialist", "other")
        binding.etAppointmentType.setAdapter(
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, types)
        )
    }

    private fun updateDateTimeDisplay() {
        binding.etDateTime.setText(displayFormat.format(Date(selectedDateTimeMs)))
    }

    private fun showDatePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select Date")
            .setSelection(selectedDateTimeMs)
            .build()

        datePicker.addOnPositiveButtonClickListener { dateMs ->
            val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            cal.timeInMillis = dateMs

            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY))
                .setMinute(0)
                .setTitleText("Select Time")
                .build()

            timePicker.addOnPositiveButtonClickListener {
                val localCal = Calendar.getInstance()
                localCal.set(
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH),
                    timePicker.hour,
                    timePicker.minute,
                    0
                )
                selectedDateTimeMs = localCal.timeInMillis
                updateDateTimeDisplay()
            }
            timePicker.show(supportFragmentManager, "TIME_PICKER")
        }
        datePicker.show(supportFragmentManager, "DATE_PICKER")
    }

    private fun loadAppointmentData() {
        lifecycleScope.launch {
            viewModel.upcomingAppointments.collect { appts ->
                val appt = appts.find { it.id == appointmentId }
                appt?.let {
                    binding.etApptTitle.setText(it.title)
                    binding.etDescription.setText(it.description)
                    binding.etLocation.setText(it.location)
                    binding.etAppointmentType.setText(it.appointmentType, false)
                    binding.etNotes.setText(it.notes)
                    selectedDateTimeMs = it.dateTime
                    updateDateTimeDisplay()
                }
            }
        }
    }

    private fun saveAppointment() {
        val title = binding.etApptTitle.text.toString().trim()
        if (title.isEmpty()) {
            binding.etApptTitle.error = "Title is required"
            return
        }

        val appointment = Appointment(
            id = appointmentId,
            personId = 1L,
            title = title,
            description = binding.etDescription.text.toString().trim(),
            dateTime = selectedDateTimeMs,
            location = binding.etLocation.text.toString().trim(),
            appointmentType = binding.etAppointmentType.text.toString().trim().ifEmpty { "general" },
            notes = binding.etNotes.text.toString().trim()
        )

        viewModel.saveAppointment(appointment)
        finish()
    }
}
