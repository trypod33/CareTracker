package com.caretracker.ui.habits

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.caretracker.data.models.Habit
import com.caretracker.databinding.ActivityAddEditHabitBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddEditHabitActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditHabitBinding
    private val viewModel: HabitViewModel by viewModels()
    private var habitId: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditHabitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Key matches what HabitsFragment passes: putExtra("habitId", habit.id)
        habitId = intent.getLongExtra("habitId", 0L)
        supportActionBar?.title = if (habitId != 0L) "Edit Habit" else "Add Habit"

        setupDropdowns()

        if (habitId != 0L) loadHabitData()

        binding.btnSaveHabit.setOnClickListener { saveHabit() }
    }

    private fun setupDropdowns() {
        val frequencies = arrayOf("daily", "weekly", "weekdays", "weekends")
        binding.etFrequency.setAdapter(
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, frequencies)
        )

        // oz and ml added so the quick-log button appears for water/liquid habits
        val units = arrayOf("times", "oz", "ml", "minutes", "hours", "steps", "glasses", "cups", "calories", "pages", "reps")
        binding.etUnit.setAdapter(
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, units)
        )
    }

    private fun loadHabitData() {
        lifecycleScope.launch {
            viewModel.habits.collect { habits ->
                val habit = habits.find { it.id == habitId }
                habit?.let {
                    binding.etHabitName.setText(it.name)
                    binding.etDescription.setText(it.description)
                    binding.etFrequency.setText(it.frequency, false)
                    binding.etTargetValue.setText(it.targetValue.toString())
                    binding.etUnit.setText(it.unit, false)
                }
            }
        }
    }

    private fun saveHabit() {
        val name = binding.etHabitName.text.toString().trim()
        if (name.isEmpty()) {
            binding.etHabitName.error = "Habit name is required"
            return
        }

        val habit = Habit(
            id = habitId,
            personId = 1L,
            name = name,
            description = binding.etDescription.text.toString().trim(),
            frequency = binding.etFrequency.text.toString().trim().ifEmpty { "daily" },
            targetValue = binding.etTargetValue.text.toString().toIntOrNull() ?: 1,
            unit = binding.etUnit.text.toString().trim().ifEmpty { "times" },
            startDate = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
                .format(java.util.Date())
        )

        viewModel.saveHabit(habit)
        finish()
    }
}
