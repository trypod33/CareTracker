package com.caretracker.ui.contacts

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.caretracker.databinding.ActivityAddEditContactBinding

class AddEditContactActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddEditContactBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditContactBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Add Contact"
        binding.btnSaveContact.setOnClickListener { finish() }
    }
}
