package com.caretracker.ui.medications

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.caretracker.data.models.Medication
import com.caretracker.databinding.ActivityAddEditMedBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddEditMedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditMedBinding
    private val viewModel: MedicationViewModel by viewModels()
    private var medId: Long = 0L
    private var personId: Long = 1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditMedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        medId    = intent.getLongExtra("MED_ID", 0L)
        personId = intent.getLongExtra("PERSON_ID", 1L)

        // Tell the ViewModel which person we're working with so that
        // the medications StateFlow emits the right person's list.
        viewModel.setPersonId(personId)

        supportActionBar?.title = if (medId != 0L) "Edit Medication" else "Add Medication"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupDropdowns()
        if (medId != 0L) loadMedData()
        binding.btnSaveMed.setOnClickListener { saveMed() }
    }

    override fun onSupportNavigateUp(): Boolean { finish(); return true }

    private fun setupDropdowns() {
        val units = arrayOf("mg", "mcg", "g", "ml", "IU", "units", "tablet", "capsule")
        binding.etUnit.setAdapter(
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, units)
        )
        val frequencies = arrayOf("daily", "twice_daily", "three_times_daily", "every_x_hours", "weekly", "as_needed")
        binding.etFrequency.setAdapter(
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, frequencies)
        )
    }

    private fun loadMedData() {
        lifecycleScope.launch {
            // Use direct DAO lookup by ID — avoids depending on the person-filtered
            // StateFlow which requires setPersonId() to have already propagated.
            val med = viewModel.getMedicationByIdDirect(medId)
            med?.let {
                personId = it.personId   // always preserve the original owner
                binding.etMedName.setText(it.name)
                binding.etDosage.setText(it.dosage)
                binding.etUnit.setText(it.unit, false)
                binding.etFrequency.setText(it.frequency, false)
                binding.etPillsRemaining.setText(it.pillsRemaining.toString())
                binding.etInstructions.setText(it.instructions)
                binding.etNotes.setText(it.notes)
            }
        }
    }

    private fun saveMed() {
        val name = binding.etMedName.text.toString().trim()
        if (name.isEmpty()) {
            binding.etMedName.error = "Medication name is required"
            return
        }
        val medication = Medication(
            id       = medId,
            personId = personId,   // correctly stamped to the selected person
            name     = name,
            dosage   = binding.etDosage.text.toString().trim(),
            unit     = binding.etUnit.text.toString().trim().ifEmpty { "mg" },
            frequency = binding.etFrequency.text.toString().trim().ifEmpty { "daily" },
            pillsRemaining = binding.etPillsRemaining.text.toString().toIntOrNull() ?: 0,
            instructions = binding.etInstructions.text.toString().trim(),
            notes    = binding.etNotes.text.toString().trim()
        )
        viewModel.saveMedication(medication)
        finish()
    }
}
