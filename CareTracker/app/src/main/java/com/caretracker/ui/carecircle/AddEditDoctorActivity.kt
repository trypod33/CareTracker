package com.caretracker.ui.carecircle

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.caretracker.data.models.Doctor
import com.caretracker.databinding.ActivityAddEditDoctorBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddEditDoctorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditDoctorBinding
    private val viewModel: DoctorViewModel by viewModels()
    private var doctorId: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditDoctorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        doctorId = intent.getLongExtra("DOCTOR_ID", 0L)
        supportActionBar?.title = if (doctorId != 0L) "Edit Doctor" else "Add Doctor"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (doctorId != 0L) loadDoctorData()

        binding.btnSaveDoctor.setOnClickListener { saveDoctor() }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun loadDoctorData() {
        lifecycleScope.launch {
            viewModel.allDoctors.collect { doctors ->
                val doctor = doctors.find { it.id == doctorId }
                doctor?.let {
                    binding.etDoctorName.setText(it.name)
                    binding.etSpecialty.setText(it.specialty)
                    binding.etPhone.setText(it.phone)
                    binding.etAddress.setText(it.address)
                    binding.etNotes.setText(it.notes)
                }
            }
        }
    }

    private fun saveDoctor() {
        val name = binding.etDoctorName.text.toString().trim()
        if (name.isEmpty()) {
            binding.etDoctorName.error = "Doctor name is required"
            return
        }
        val doctor = Doctor(
            id = doctorId,
            name = name,
            specialty = binding.etSpecialty.text.toString().trim(),
            phone = binding.etPhone.text.toString().trim(),
            address = binding.etAddress.text.toString().trim(),
            notes = binding.etNotes.text.toString().trim()
        )
        viewModel.saveDoctor(doctor)
        finish()
    }
}
