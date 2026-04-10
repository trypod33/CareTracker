package com.caretracker.ui.people

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.caretracker.data.models.Person
import com.caretracker.databinding.ActivityAddEditPersonBinding
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddEditPersonActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditPersonBinding
    private val viewModel: PersonViewModel by viewModels()
    private var personId: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditPersonBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Add Person"

        personId = intent.getLongExtra("PERSON_ID", 0L)
        if (personId != 0L) {
            supportActionBar?.title = "Edit Person"
            loadPersonData()
        }

        setupDropdowns()

        binding.etBirthDate.setOnClickListener { showDatePicker() }
        binding.btnSavePerson.setOnClickListener { savePerson() }
    }

    private fun setupDropdowns() {
        val relationships = arrayOf("Parent", "Child", "Spouse", "Sibling", "Grandparent", "Friend", "Other")
        binding.etRelationship.setAdapter(
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, relationships)
        )

        val roles = arrayOf("Self", "Care Receiver", "Primary Caregiver", "Secondary Caregiver", "Emergency Contact")
        binding.etPersonRole.setAdapter(
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, roles)
        )
    }

    private fun showDatePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select Birth Date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        datePicker.addOnPositiveButtonClickListener { selection ->
            // MaterialDatePicker returns UTC midnight.
            // Both the Calendar AND the formatter must use UTC so the
            // date is not shifted back by the local timezone offset.
            val utc = TimeZone.getTimeZone("UTC")
            val calendar = Calendar.getInstance(utc)
            calendar.timeInMillis = selection
            val format = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).apply {
                timeZone = utc  // <-- this is the critical fix
            }
            binding.etBirthDate.setText(format.format(calendar.time))
        }

        datePicker.show(supportFragmentManager, "DATE_PICKER")
    }

    private fun loadPersonData() {
        lifecycleScope.launch {
            viewModel.allPeople.collect { people ->
                val person = people.find { it.id == personId }
                person?.let {
                    binding.etPersonName.setText(it.name)
                    binding.etRelationship.setText(it.relationship, false)
                    binding.etPersonRole.setText(it.role, false)
                    binding.etBirthDate.setText(it.birthDate)
                    binding.etPersonNotes.setText(it.notes)
                }
            }
        }
    }

    private fun savePerson() {
        val name = binding.etPersonName.text.toString().trim()
        val relationship = binding.etRelationship.text.toString().trim()
        val role = binding.etPersonRole.text.toString().trim()
        val birthDate = binding.etBirthDate.text.toString().trim()
        val notes = binding.etPersonNotes.text.toString().trim()

        if (name.isEmpty()) {
            binding.etPersonName.error = "Name is required"
            return
        }

        val person = Person(
            id = personId,
            name = name,
            birthDate = birthDate,
            relationship = relationship,
            role = role,
            notes = notes,
            color = "#38bdf8",
            avatar = getAvatarForRole(role)
        )

        viewModel.savePerson(person)
        finish()
    }

    private fun getAvatarForRole(role: String): String {
        return when (role) {
            "Self"               -> "\uD83D\uDE0A"
            "Care Receiver"      -> "\uD83D\uDC75"
            "Primary Caregiver"  -> "\uD83C\uDFE0"
            "Secondary Caregiver"-> "\uD83D\uDC4B"
            else                 -> "\uD83D\uDC64"
        }
    }
}
