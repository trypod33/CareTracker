package com.caretracker.ui.calendar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.caretracker.databinding.ActivityAddEditAppointmentBinding

class AddEditAppointmentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddEditAppointmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditAppointmentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Add Appointment"
        binding.btnSaveAppointment.setOnClickListener { finish() }
    }
}
