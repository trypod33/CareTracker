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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddEditDoctorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditDoctorBinding
    private val viewModel: DoctorViewModel by viewModels()
    private var doctorId: Long = 0L
    private var selectedAvatar: String = "\uD83E\uFA7A"
    private var previousLinkedPersonIds: Set<Long> = emptySet()

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

        // Step 1: one-shot load of existing linked person IDs (edit mode only).
        // Must run before collectLatest below so previousLinkedPersonIds is set
        // before rebuildCheckboxes() is called for the first time.
        if (doctorId != 0L) {
            lifecycleScope.launch {
                previousLinkedPersonIds = viewModel.getLinkedPersonIds(doctorId).toSet()
                // Force a rebuild now that IDs are known
                rebuildCheckboxes(viewModel.allPeople.value)
            }
        }

        // Step 2: observe allPeople — rebuild checkboxes whenever list changes.
        // collectLatest keeps it live so members added later also appear.
        lifecycleScope.launch {
            viewModel.allPeople.collectLatest { people ->
                rebuildCheckboxes(people)
            }
        }

        // Step 3: load doctor fields for edit (waits until StateFlow has data).
        if (doctorId != 0L) {
            lifecycleScope.launch {
                val doctor = viewModel.allDoctors
                    .first { it.isNotEmpty() }
                    .find { it.id == doctorId }
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

        binding.btnSaveDoctor.setOnClickListener { saveDoctor() }
    }

    override fun onSupportNavigateUp(): Boolean { finish(); return true }

    // ── Patient checkboxes ──────────────────────────────────────────

    private fun rebuildCheckboxes(people: List<Person>) {
        binding.layoutPatients.removeAllViews()
        personCheckBoxes.clear()

        if (people.isEmpty()) {
            binding.layoutPatients.addView(TextView(this).apply {
                text = "No members added yet. Add members in Care Circle first."
                setPadding(4, 8, 4, 8)
            })
            return
        }

        people.forEach { person ->
            val cb = CheckBox(this).apply {
                text = "${person.avatar}  ${person.name}" +
                    if (!person.role.isNullOrBlank()) "  \u2022  ${person.role}" else ""
                tag = person.id
                isChecked = previousLinkedPersonIds.contains(person.id)
            }
            personCheckBoxes[person.id] = cb
            binding.layoutPatients.addView(cb)
        }
    }

    // ── Emoji avatar rows ──────────────────────────────────────────

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

    // ── Save ────────────────────────────────────────────────────────────

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

            val savedId: Long = if (doctorId == 0L) {
                viewModel.saveDoctorAndGetId(doctor)
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
