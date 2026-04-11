package com.caretracker.ui.medications

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.caretracker.data.models.Medication
import com.caretracker.databinding.ActivityAddEditMedBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Calendar

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

        viewModel.setPersonId(personId)

        supportActionBar?.title = if (medId != 0L) "Edit Medication" else "Add Medication"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupDropdowns()
        setupColorPicker()
        setupDatePicker()
        if (medId != 0L) loadMedData()

        binding.btnSaveMed.setOnClickListener { saveMed() }
        binding.btnCancelMed.setOnClickListener { finish() }
    }

    override fun onSupportNavigateUp(): Boolean { finish(); return true }

    // -------------------------------------------------------------------------
    // Dropdowns
    // -------------------------------------------------------------------------

    private fun setupDropdowns() {
        // Unit dropdown
        val units = arrayOf("mg", "mcg", "g", "ml", "IU", "units", "tablet", "capsule")
        binding.etUnit.setAdapter(
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, units)
        )

        // Form dropdown
        val forms = arrayOf("Tablet", "Capsule", "Liquid", "Injection", "Patch",
                            "Inhaler", "Drops", "Cream", "Other")
        binding.etForm.setAdapter(
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, forms)
        )

        // Person dropdown — populated from the ViewModel's people list
        lifecycleScope.launch {
            viewModel.getAllPeople().collect { people ->
                val names = people.map { it.name }.toTypedArray()
                binding.etPersonName.setAdapter(
                    ArrayAdapter(this@AddEditMedActivity,
                        android.R.layout.simple_dropdown_item_1line, names)
                )
                // Pre-select the person passed in via intent
                if (personId != 0L) {
                    people.firstOrNull { it.id == personId }?.let {
                        binding.etPersonName.setText(it.name, false)
                    }
                }
            }
        }
    }

    // -------------------------------------------------------------------------
    // Color picker — add colored circle buttons programmatically
    // -------------------------------------------------------------------------

    private val cardColors = listOf(
        "#4F98A3", "#6FAF6F", "#E8AF34", "#DD6974",
        "#A86FDF", "#5591C7", "#BB653B", "#797876"
    )
    private var selectedColor = cardColors[0]

    private fun setupColorPicker() {
        val size = resources.getDimensionPixelSize(android.R.dimen.app_icon_size) // ~48dp
        val gap  = resources.getDimensionPixelSize(android.R.dimen.notification_large_icon_width) / 8
        cardColors.forEach { hex ->
            val btn = android.widget.ImageButton(this).apply {
                layoutParams = android.widget.LinearLayout.LayoutParams(size, size).also {
                    it.marginEnd = gap
                }
                val drawable = android.graphics.drawable.GradientDrawable().also { d ->
                    d.shape = android.graphics.drawable.GradientDrawable.OVAL
                    d.setColor(android.graphics.Color.parseColor(hex))
                }
                background = drawable
                contentDescription = "Color $hex"
                setOnClickListener {
                    selectedColor = hex
                    // visually mark selected
                    (binding.layoutColorPicker as android.widget.LinearLayout)
                        .forEach { child ->
                            (child as? android.widget.ImageButton)?.alpha = 0.4f
                        }
                    alpha = 1.0f
                }
            }
            if (hex == selectedColor) btn.alpha = 1.0f else btn.alpha = 0.4f
            binding.layoutColorPicker.addView(btn)
        }
    }

    // -------------------------------------------------------------------------
    // Date picker for Start Date field
    // -------------------------------------------------------------------------

    private fun setupDatePicker() {
        binding.etStartDate.setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(
                this,
                { _, y, m, d -> binding.etStartDate.setText("%04d-%02d-%02d".format(y, m + 1, d)) },
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    // -------------------------------------------------------------------------
    // Load existing med into form (edit mode)
    // -------------------------------------------------------------------------

    private fun loadMedData() {
        lifecycleScope.launch {
            val med = viewModel.getMedicationByIdDirect(medId)
            med?.let {
                personId = it.personId
                binding.etMedName.setText(it.name)
                binding.etGenericName.setText(it.notes)   // notes repurposed as generic name display
                binding.etDosage.setText(it.dosage)
                binding.etUnit.setText(it.unit, false)
                binding.etForm.setText("Tablet", false)   // default; extend model if needed
                binding.etFrequency.setText(it.frequency.replace("_", " "))
                binding.etTimesPerDay.setText(it.pillsPerDose.toString())
                binding.etScheduledTimes.setText(it.timesOfDay.joinToString(","))
                binding.etInstructions.setText(it.instructions)
                binding.etPrescriber.setText("")          // extend model if needed
                binding.etPharmacy.setText("")             // extend model if needed
                binding.etRxNumber.setText("")             // extend model if needed
                binding.etPillsRemaining.setText(it.pillsRemaining.toString())
                binding.etPillsPerRefill.setText(it.pillsPerDose.toString())
                binding.etRefillReminderAt.setText(it.refillReminderAt.toString())
                binding.etStartDate.setText(it.startDate)
                selectedColor = it.color
            }
        }
    }

    // -------------------------------------------------------------------------
    // Save
    // -------------------------------------------------------------------------

    private fun saveMed() {
        val name = binding.etMedName.text.toString().trim()
        if (name.isEmpty()) {
            binding.etMedName.error = "Medication name is required"
            return
        }

        // Resolve personId from the dropdown selection if the user changed it
        // (The ViewModel holds the person list; for now we keep the pre-set personId
        //  unless the user typed a different name — a full lookup can be added later.)

        val scheduledTimesList = binding.etScheduledTimes.text.toString()
            .split(",")
            .map { it.trim() }
            .filter { it.isNotEmpty() }

        val medication = Medication(
            id                 = medId,
            personId           = personId,
            name               = name,
            dosage             = binding.etDosage.text.toString().trim(),
            unit               = binding.etUnit.text.toString().trim().ifEmpty { "mg" },
            frequency          = binding.etFrequency.text.toString().trim().replace(" ", "_")
                                     .ifEmpty { "daily" },
            timesOfDay         = scheduledTimesList,
            instructions       = binding.etInstructions.text.toString().trim(),
            pillsRemaining     = binding.etPillsRemaining.text.toString().toIntOrNull() ?: 0,
            pillsPerDose       = binding.etPillsPerRefill.text.toString().toIntOrNull() ?: 1,
            refillReminderAt   = binding.etRefillReminderAt.text.toString().toIntOrNull() ?: 7,
            startDate          = binding.etStartDate.text.toString().trim(),
            color              = selectedColor,
            notes              = binding.etGenericName.text.toString().trim()
        )
        viewModel.saveMedication(medication)
        finish()
    }
}
