package com.caretracker.ui.habits

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.caretracker.databinding.ActivityAddEditHabitBinding

class AddEditHabitActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddEditHabitBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditHabitBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Add Habit"
        binding.btnSaveHabit.setOnClickListener { finish() }
    }
}
