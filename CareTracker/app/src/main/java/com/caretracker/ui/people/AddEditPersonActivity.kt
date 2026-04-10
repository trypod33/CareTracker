package com.caretracker.ui.people

import android.os.Bundle
import android.view.Gravity
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.caretracker.R
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
    private var selectedAvatar: String = "\uD83D\uDC64" // default 👤

    // Emoji sets by category
    private val peopleEmojis = listOf(
        "\uD83D\uDC64", // 👤
        "\uD83D\uDE0A", // 😊
        "\uD83D\uDC75", // 👵
        "\uD83D\uDC74", // 👴
        "\uD83D\uDC69", // 👩
        "\uD83D\uDC68", // 👨
        "\uD83D\uDC67", // 👧
        "\uD83D\uDC66", // 👦
        "\uD83D\uDC76", // 👶
        "\uD83D\uDC71" // 💱
    )
    private val healthEmojis = listOf(
        "\uD83C\uDFE0", // 🏠
        "\u2764\uFE0F",  // ❤️
        "\uD83D\uDC4B", // 👋
        "\uD83E\uDD1D", // 🤝
        "\uD83D\uDCAA", // 💪
        "\uD83C\uDF1F", // 🌟
        "\uD83D\uDC9A", // 💚
        "\uD83D\uDC99", // 💙
        "\uD83D\uDC9C", // 💜
        "\uD83D\uDC9B"  // 💛
    )
    private val otherEmojis = listOf(
        "\u2605",        // ★
        "\uD83C\uDF08", // 🌈
        "\uD83C\uDF3B", // 🌻
        "\uD83D\uDC3E", // 🐾
        "\uD83C\uDFAE", // 🎮
        "\uD83D\uDCDA", // 📚
        "\uD83C\uDFB5", // 🎵
        "\uD83D\uDE97", // 🚗
        "\uD83C\uDF54", // 🍔
        "\u26BD"         // ⚽
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditPersonBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        personId = intent.getLongExtra("PERSON_ID", 0L)
        supportActionBar?.title = if (personId != 0L) "Edit Person" else "Add Person"
        if (personId != 0L) loadPersonData()

        setupDropdowns()
        buildEmojiRows()

        binding.etBirthDate.setOnClickListener { showDatePicker() }
        binding.btnSavePerson.setOnClickListener { savePerson() }
    }

    override fun onSupportNavigateUp(): Boolean { finish(); return true }

    private fun buildEmojiRows() {
        addEmojisToRow(binding.rowPeople, peopleEmojis)
        addEmojisToRow(binding.rowHealth, healthEmojis)
        addEmojisToRow(binding.rowOther, otherEmojis)
        // Highlight default
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
        refreshRow(binding.rowPeople, peopleEmojis)
        refreshRow(binding.rowHealth, healthEmojis)
        refreshRow(binding.rowOther, otherEmojis)
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
            val utc = TimeZone.getTimeZone("UTC")
            val calendar = Calendar.getInstance(utc)
            calendar.timeInMillis = selection
            val format = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).apply {
                timeZone = utc
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
                    // Restore saved avatar selection
                    if (it.avatar.isNotEmpty()) {
                        selectedAvatar = it.avatar
                        updatePreview(selectedAvatar)
                        refreshAllRows()
                    }
                }
            }
        }
    }

    private fun savePerson() {
        val name = binding.etPersonName.text.toString().trim()
        if (name.isEmpty()) {
            binding.etPersonName.error = "Name is required"
            return
        }
        val person = Person(
            id = personId,
            name = name,
            birthDate = binding.etBirthDate.text.toString().trim(),
            relationship = binding.etRelationship.text.toString().trim(),
            role = binding.etPersonRole.text.toString().trim(),
            notes = binding.etPersonNotes.text.toString().trim(),
            color = "#38bdf8",
            avatar = selectedAvatar
        )
        viewModel.savePerson(person)
        finish()
    }
}
