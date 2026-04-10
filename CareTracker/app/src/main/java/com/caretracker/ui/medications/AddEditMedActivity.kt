package com.caretracker.ui.medications

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.caretracker.databinding.ActivityAddEditMedBinding

class AddEditMedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddEditMedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditMedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Add Medication"
        binding.btnSaveMed.setOnClickListener { finish() }
    }
}
