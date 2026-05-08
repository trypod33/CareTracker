package com.caretracker.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.caretracker.CareTrackerApp
import com.caretracker.R
import com.caretracker.data.entities.UserEntity
import com.caretracker.databinding.ActivityMainBinding
import com.caretracker.security.PasswordHasher
import com.caretracker.ui.auth.LoginActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val app get() = application as CareTrackerApp
    private var userList: List<UserEntity> = emptyList()
    private var spinnerReady = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNav.setupWithNavController(navController)

        val savedUserId = intent.getLongExtra("user_id", -1L)
        if (savedUserId != -1L) app.currentUserId = savedUserId

        binding.btnLogout.setOnClickListener { signOut() }
        binding.btnMore.setOnClickListener { showOverflowMenu(it) }

        lifecycleScope.launch {
            app.repository.getAllUsers().collect { users ->
                userList = users
                rebuildSpinner()
            }
        }
    }

    private fun showOverflowMenu(anchor: View) {
        PopupMenu(this, anchor).apply {
            menuInflater.inflate(R.menu.main_overflow_menu, menu)
            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_delete_person -> {
                        confirmDeleteCurrentPerson()
                        true
                    }
                    else -> false
                }
            }
            show()
        }
    }

    private fun rebuildSpinner() {
        spinnerReady = false
        val labels = userList.map { it.displayName ?: it.username } + listOf("+ Add Person")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, labels).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        binding.spinnerUser.adapter = adapter

        val currentIndex = userList.indexOfFirst { it.id == app.currentUserId }
        if (currentIndex >= 0) binding.spinnerUser.setSelection(currentIndex)

        spinnerReady = true

        binding.spinnerUser.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (!spinnerReady) return

                if (position == userList.size) {
                    val currentIndex = userList.indexOfFirst { it.id == app.currentUserId }
                    if (currentIndex >= 0) binding.spinnerUser.setSelection(currentIndex)
                    showAddPersonDialog()
                } else {
                    val selected = userList[position]
                    if (selected.id != app.currentUserId) {
                        app.currentUserId = selected.id
                        getSharedPreferences("caretracker", Context.MODE_PRIVATE)
                            .edit()
                            .putLong("current_user_id", selected.id)
                            .apply()
                    }
                }
            }
        }
    }

    private fun showAddPersonDialog() {
        val input = android.widget.EditText(this).apply {
            hint = "Name"
            setPadding(48, 24, 48, 8)
        }

        val passInput = android.widget.EditText(this).apply {
            hint = "Password"
            inputType = android.text.InputType.TYPE_CLASS_TEXT or
                android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
            setPadding(48, 8, 48, 24)
        }

        val container = android.widget.LinearLayout(this).apply {
            orientation = android.widget.LinearLayout.VERTICAL
            addView(input)
            addView(passInput)
        }

        AlertDialog.Builder(this)
            .setTitle("Add Person")
            .setView(container)
            .setPositiveButton("Add") { _, _ ->
                val name = input.text.toString().trim()
                val pass = passInput.text.toString()

                if (name.isNotBlank() && pass.isNotBlank()) {
                    lifecycleScope.launch {
                        val user = UserEntity(
                            username = name.lowercase().replace(" ", "_"),
                            email = "${name.lowercase().replace(" ", "_")}@local.app",
                            passwordHash = PasswordHasher.hash(pass),
                            displayName = name,
                            role = "user"
                        )
                        val newId = app.repository.insertUser(user)
                        app.currentUserId = newId
                        getSharedPreferences("caretracker", Context.MODE_PRIVATE)
                            .edit()
                            .putLong("current_user_id", newId)
                            .apply()
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun confirmDeleteCurrentPerson() {
        val current = userList.firstOrNull { it.id == app.currentUserId } ?: return

        AlertDialog.Builder(this)
            .setTitle("Delete person?")
            .setMessage("Delete ${current.displayName ?: current.username} and all their data? This cannot be undone.")
            .setPositiveButton("Delete") { _, _ ->
                lifecycleScope.launch {
                    app.repository.deleteUserAndData(current)

                    val remaining = app.repository.getAllUsers().first()
                    if (remaining.isEmpty()) {
                        getSharedPreferences("caretracker", Context.MODE_PRIVATE)
                            .edit()
                            .remove("current_user_id")
                            .apply()
                        app.currentUserId = -1L
                        startActivity(Intent(this@MainActivity, LoginActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        })
                        finish()
                    } else {
                        val nextUser = remaining.first()
                        app.currentUserId = nextUser.id
                        getSharedPreferences("caretracker", Context.MODE_PRIVATE)
                            .edit()
                            .putLong("current_user_id", nextUser.id)
                            .apply()
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun signOut() {
        getSharedPreferences("caretracker", Context.MODE_PRIVATE)
            .edit()
            .remove("current_user_id")
            .apply()

        app.currentUserId = -1L

        val intent = Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }
}
