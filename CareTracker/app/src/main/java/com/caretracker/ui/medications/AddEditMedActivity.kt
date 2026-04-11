package com.caretracker.ui.medications

import android.app.DatePickerDialog
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.caretracker.data.models.Medication
import com.caretracker.databinding.ActivityAddEditMedBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Calendar

@AndroidEntryPoint
class AddEditMedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditMedBinding
    private val viewModel: MedicationViewModel by viewModels()
    private var medId: Long = 0L
    private var personId: Long = 1L

    private val cardColors = listOf(
        "#4F98A3", "#6FAF6F", "#E8AF34", "#DD6974",
        "#A86FDF", "#5591C7", "#BB653B", "#797876"
    )
    private var selectedColor = cardColors[0]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditMedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        medId    = intent.getLongExtra("MED_ID", 0L)
        personId = intent.getLongExtra("PERSON_ID", 1L)

        viewModel.setPersonId(personId)

        supportActionBar?.title = if (medId != 0L) "Edit Medication" else "Add Medication"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupStaticDropdowns()
        setupPeopleDropdown()
        setupColorPicker()
        setupDatePicker()
        if (medId != 0L) loadMedData()

        binding.btnSaveMed.setOnClickListener { saveMed() }
        binding.btnCancelMed.setOnClickListener { finish() }
    }

    override fun onSupportNavigateUp(): Boolean { finish(); return true }

    // ── Static dropdowns (unit / form) ────────────────────────────────────────

    private fun setupStaticDropdowns() {
        val units = arrayOf("mg", "mcg", "g", "ml", "IU", "units", "tablet", "capsule")
        binding.etUnit.setAdapter(
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, units)
        )
        val forms = arrayOf("Tablet", "Capsule", "Liquid", "Injection",
                            "Patch", "Inhaler", "Drops", "Cream", "Other")
        binding.etForm.setAdapter(
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, forms)
        )
    }

    // ── People dropdown — collected from ViewModel StateFlow ─────────────────

    private fun setupPeopleDropdown() {
        lifecycleScope.launch {
            viewModel.allPeople.collectLatest { people ->
                val names = ArrayList<String>(people.map { person -> person.name })
                binding.etPersonName.setAdapter(
                    ArrayAdapter(this@AddEditMedActivity,
                        android.R.layout.simple_dropdown_item_1line, names)
                )
                // Pre-select the person passed via intent
                if (personId != 0L) {
                    people.firstOrNull { person -> person.id == personId }?.let { person ->
                        binding.etPersonName.setText(person.name, false)
                    }
                }
            }
        }
    }

    // ── Color picker ─────────────────────────────────────────────────────────

    private fun setupColorPicker() {
        val sizePx = (40 * resources.displayMetrics.density).toInt()
        val gapPx  = (8  * resources.displayMetrics.density).toInt()
        cardColors.forEach { hex ->
            val btn = ImageButton(this).apply {
                val lp = LinearLayout.LayoutParams(sizePx, sizePx)
                lp.marginEnd = gapPx
                layoutParams = lp
                val circle = GradientDrawable().apply {
                    shape = GradientDrawable.OVAL
                    setColor(Color.parseColor(hex))
                }
                background = circle
                contentDescription = "Card color $hex"
                alpha = if (hex == selectedColor) 1.0f else 0.4f
                setOnClickListener {
                    selectedColor = hex
                    // Reset all swatches then highlight this one
                    val container = binding.layoutColorPicker as LinearLayout
                    for (i in 0 until container.childCount) {
                        (container.getChildAt(i) as? ImageButton)?.alpha = 0.4f
                    }
                    alpha = 1.0f
                }
            }
            binding.layoutColorPicker.addView(btn)
        }
    }

    // ── Date picker ──────────────────────────────────────────────────────────

    private fun setupDatePicker() {
        binding.etStartDate.setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(
                this,
                { _, y, m, d ->
                    binding.etStartDate.setText("%04d-%02d-%02d".format(y, m + 1, d))
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    // ── Load existing med into form (edit mode) ───────────────────────────────

    private fun loadMedData() {
        lifecycleScope.launch {
            val med = viewModel.getMedicationByIdDirect(medId)
            med?.let { m ->
                personId = m.personId
                binding.etMedName.setText(m.name)
                binding.etGenericName.setText(m.notes)
                binding.etDosage.setText(m.dosage)
                binding.etUnit.setText(m.unit, false)
                binding.etForm.setText("Tablet", false)
                binding.etFrequency.setText(m.frequency.replace("_", " "))
                binding.etTimesPerDay.setText(m.pillsPerDose.toString())
                binding.etScheduledTimes.setText(m.timesOfDay.joinToString(","))
                binding.etInstructions.setText(m.instructions)
                binding.etPrescriber.setText("")
                binding.etPharmacy.setText("")
                binding.etRxNumber.setText("")
                binding.etPillsRemaining.setText(m.pillsRemaining.toString())
                binding.etPillsPerRefill.setText(m.pillsPerDose.toString())
                binding.etRefillReminderAt.setText(m.refillReminderAt.toString())
                binding.etStartDate.setText(m.startDate)
                selectedColor = m.color
            }
        }
    }

    // ── Save ─────────────────────────────────────────────────────────────────

    private fun saveMed() {
        val name = binding.etMedName.text.toString().trim()
        if (name.isEmpty()) {
            binding.etMedName.error = "Medication name is required"
            return
        }

        val scheduledTimes = binding.etScheduledTimes.text.toString()
            .split(",")
            .map { it.trim() }
            .filter { it.isNotEmpty() }

        val medication = Medication(
            id               = medId,
            personId         = personId,
            name             = name,
            dosage           = binding.etDosage.text.toString().trim(),
            unit             = binding.etUnit.text.toString().trim().ifEmpty { "mg" },
            frequency        = binding.etFrequency.text.toString().trim()
                                   .replace(" ", "_").ifEmpty { "daily" },
            timesOfDay       = scheduledTimes,
            instructions     = binding.etInstructions.text.toString().trim(),
            pillsRemaining   = binding.etPillsRemaining.text.toString().toIntOrNull() ?: 0,
            pillsPerDose     = binding.etPillsPerRefill.text.toString().toIntOrNull() ?: 1,
            refillReminderAt = binding.etRefillReminderAt.text.toString().toIntOrNull() ?: 7,
            startDate        = binding.etStartDate.text.toString().trim(),
            color            = selectedColor,
            notes            = binding.etGenericName.text.toString().trim()
        )
        viewModel.saveMedication(medication)
        finish()
    }
}
