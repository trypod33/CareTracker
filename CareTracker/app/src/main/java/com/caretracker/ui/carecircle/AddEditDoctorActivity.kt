package com.caretracker.ui.carecircle

import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.caretracker.R
import com.caretracker.data.models.Doctor
import com.caretracker.databinding.ActivityAddEditDoctorBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddEditDoctorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditDoctorBinding
    private val viewModel: DoctorViewModel by viewModels()
    private var doctorId: Long = 0L
    private var selectedAvatar: String = "\uD83E\uDDBA" // default 🧺 → use 🩺

    private val medicalEmojis = listOf(
        "\uD83E\uFA7A", // 🩺
        "\uD83D\uDC89", // 💉
        "\uD83D\uDC8A", // 💊
        "\uD83E\uDE7A", // 🩺 bandage
        "\uD83C\uDFE5", // 🏥
        "\u2695\uFE0F",  // ⚕️
        "\uD83E\uDDEC", // 🧬
        "\uD83D\uDD2C", // 🔬
        "\uD83E\uDDE0", // 🧠
        "\u2764\uFE0F"   // ❤️
    )
    private val peopleEmojis = listOf(
        "\uD83D\uDC68\u200D\u2695\uFE0F", // 👨‍⚕️
        "\uD83D\uDC69\u200D\u2695\uFE0F", // 👩‍⚕️
        "\uD83D\uDC64", // 👤
        "\uD83D\uDE0A", // 😊
        "\uD83D\uDC75", // 👵
        "\uD83D\uDC74", // 👴
        "\uD83D\uDC69", // 👩
        "\uD83D\uDC68", // 👨
        "\uD83E\uDD1D", // 🤝
        "\uD83C\uDF1F"  // 🌟
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditDoctorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        doctorId = intent.getLongExtra("DOCTOR_ID", 0L)
        supportActionBar?.title = if (doctorId != 0L) "Edit Doctor" else "Add Doctor"
        if (doctorId != 0L) loadDoctorData()

        buildEmojiRows()
        binding.btnSaveDoctor.setOnClickListener { saveDoctor() }
    }

    override fun onSupportNavigateUp(): Boolean { finish(); return true }

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
                layoutParams = LinearLayout.LayoutParams(size, size).also {
                    it.marginEnd = 4
                }
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

    private fun updatePreview(emoji: String) {
        binding.tvSelectedAvatar.text = emoji
    }

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
                    if (it.avatar.isNotEmpty()) {
                        selectedAvatar = it.avatar
                        updatePreview(selectedAvatar)
                        refreshAllRows()
                    }
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
            notes = binding.etNotes.text.toString().trim(),
            avatar = selectedAvatar
        )
        viewModel.saveDoctor(doctor)
        finish()
    }
}
