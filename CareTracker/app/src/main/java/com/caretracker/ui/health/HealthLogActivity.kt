package com.caretracker.ui.health

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.caretracker.databinding.ActivityHealthLogBinding

class HealthLogActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHealthLogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHealthLogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Log Health Data"
        binding.btnSaveHealthLog.setOnClickListener { finish() }
    }
}
