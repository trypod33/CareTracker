package com.caretracker.ui.carecircle

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.caretracker.R
import com.caretracker.data.models.Doctor
import com.caretracker.data.models.Person
import com.caretracker.databinding.ActivityAddEditDoctorBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddEditDoctorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditDoctorBinding
    private val viewModel: DoctorViewModel by viewModels()
    private var doctorId: Long = 0L
    private var selectedAvatar: String = "\uD83E\uFA7A"
    private var previousLinkedPersonIds: Set<Long> = emptySet()

    // Checkboxes map: personId -> CheckBox view
    private val personCheckBoxes = mutableMapOf<Long, CheckBox>()

    private val medicalEmojis = listOf(
        "\uD83E\uFA7A", "\uD83D\uDC89", "\uD83D\uDC8A",
        "\uD83E\uDE7A", "\uD83C\uDFE5", "\u2695\uFE0F",
        "\uD83E\uDDEC", "\uD83D\uDD2C", "\uD83E\uDDE0", "\u2764\uFE0F"
    )
    private val peopleEmojis = listOf(
        "\uD83D\uDC68\u200D\u2695\uFE0F", "\uD83D\uDC69\u200D\u2695\uFE0F",
        "\uD83D\uDC64", "\uD83D\uDE0A", "\uD83D\uDC75",
        "\uD83D\uDC74", "\uD83D\uDC69", "\uD83D\uDC68",
        "\uD83E\uDD1D", "\uD83C\uDF1F"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditDoctorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        doctorId = intent.getLongExtra("DOCTOR_ID", 0L)
        supportActionBar?.title = if (doctorId != 0L) "Edit Doctor" else "Add Doctor"

        buildEmojiRows()
        buildPatientCheckboxes()

        if (doctorId != 0L) loadDoctorData()

        binding.btnSaveDoctor.setOnClickListener { saveDoctor() }
    }

    override fun onSupportNavigateUp(): Boolean { finish(); return true }

    // ── Patient checkboxes ────────────────────────────────────────────────────

    private fun buildPatientCheckboxes() {
        lifecycleScope.launch {
            viewModel.allPeople.collect { people ->
                binding.layoutPatients.removeAllViews()
                personCheckBoxes.clear()
                people.forEach { person ->
                    val cb = CheckBox(this@AddEditDoctorActivity).apply {
                        text = "${person.avatar}  ${person.name}  \u2022  ${person.role ?: ""}"
                        tag = person.id
                        isChecked = personCheckBoxes[person.id]?.isChecked ?: false
                    }
                    personCheckBoxes[person.id] = cb
                    binding.layoutPatients.addView(cb)
                }
                // Re-check previously linked people after list reloads
                previousLinkedPersonIds.forEach { id ->
                    personCheckBoxes[id]?.isChecked = true
                }
            }
        }
    }

    // ── Emoji avatar rows ─────────────────────────────────────────────────────

    private fun buildEmojiRows() {
        addEmojisToRow(binding.rowMedical, medicalEmojis)
        addEmojisToRow(binding.rowPeople, peopleEmojis)
        updatePreview(selectedAvatar)
    }

    private fun addEmojisToRow(row: LinearLayout, emojis: List<String>) {
        val size = resources.getDimensionPixelSize(R.dimen.avatar_cell_size)
        emojis.forEach { emoji ->
            val tv = TextView(this).apply {
                text = emoji
                textSize = 24f
                gravity = Gravity.CENTER
                layoutParams = LinearLayout.LayoutParams(size, size).also { it.marginEnd = 4 }
                background = getDrawable(
                    if (emoji == selectedAvatar) R.drawable.bg_avatar_selected
                    else R.drawable.bg_avatar_unselected
                )
                setOnClickListener {
                    selectedAvatar = emoji
                    updatePreview(emoji)
                    refreshAllRows()
                }
            }
            row.addView(tv)
        }
    }

    private fun updatePreview(emoji: String) { binding.tvSelectedAvatar.text = emoji }

    private fun refreshAllRows() {
        refreshRow(binding.rowMedical, medicalEmojis)
        refreshRow(binding.rowPeople, peopleEmojis)
    }

    private fun refreshRow(row: LinearLayout, emojis: List<String>) {
        for (i in 0 until row.childCount) {
            val tv = row.getChildAt(i) as? TextView ?: continue
            val emoji = emojis.getOrNull(i) ?: continue
            tv.background = getDrawable(
                if (emoji == selectedAvatar) R.drawable.bg_avatar_selected
                else R.drawable.bg_avatar_unselected
            )
        }
    }

    // ── Load existing doctor ──────────────────────────────────────────────────

    private fun loadDoctorData() {
        lifecycleScope.launch {
            // Load doctor fields
            viewModel.allDoctors.collect { doctors ->
                val doctor = doctors.find { it.id == doctorId }
                doctor?.let {
                    binding.etDoctorName.setText(it.name)
                    binding.etSpecialty.setText(it.specialty)
                    binding.etPhone.setText(it.phone)
                    binding.etAddress.setText(it.address)
                    binding.etNotes.setText(it.notes)
                    if (it.avatar.isNotEmpty()) {
                        selectedAvatar = it.avatar
                        updatePreview(selectedAvatar)
                        refreshAllRows()
                    }
                }
            }
        }

        lifecycleScope.launch {
            // Load existing patient links and pre-check boxes
            // getLinkedDoctorIds actually returns personIds linked to this doctor
            val linkedIds = viewModel.getLinkedPersonIds(doctorId)
            previousLinkedPersonIds = linkedIds.toSet()
            linkedIds.forEach { personId ->
                personCheckBoxes[personId]?.isChecked = true
            }
        }
    }

    // ── Save ──────────────────────────────────────────────────────────────────

    private fun saveDoctor() {
        val name = binding.etDoctorName.text.toString().trim()
        if (name.isEmpty()) {
            binding.etDoctorName.error = "Doctor name is required"
            return
        }

        lifecycleScope.launch {
            val doctor = Doctor(
                id = doctorId,
                name = name,
                specialty = binding.etSpecialty.text.toString().trim(),
                phone = binding.etPhone.text.toString().trim(),
                address = binding.etAddress.text.toString().trim(),
                notes = binding.etNotes.text.toString().trim(),
                avatar = selectedAvatar
            )

            // Insert/update doctor first to get the id
            val savedId: Long = if (doctorId == 0L) {
                // new doctor — insert returns the new id
                var newId = 0L
                viewModel.saveDoctor(doctor)
                // Wait briefly for the insert then get it from allDoctors
                // Better: use a suspend insert in repo
                // For now collect once to get the new id
                viewModel.allDoctors.collect { doctors ->
                    val saved = doctors.find {
                        it.name == name &&
                        it.specialty == doctor.specialty &&
                        it.phone == doctor.phone
                    }
                    if (saved != null) { newId = saved.id; return@collect }
                }
                newId
            } else {
                viewModel.saveDoctor(doctor)
                doctorId
            }

            if (savedId != 0L) {
                val selectedIds = personCheckBoxes
                    .filter { (_, cb) -> cb.isChecked }
                    .keys.toSet()
                viewModel.savePatientLinks(savedId, selectedIds, previousLinkedPersonIds)
            }

            finish()
        }
    }
}
