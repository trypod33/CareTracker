package com.caretracker.ui.auth

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.caretracker.CareTrackerApp
import com.caretracker.data.entities.UserEntity
import com.caretracker.databinding.ActivityLoginBinding
import com.caretracker.security.PasswordHasher
import com.caretracker.ui.MainActivity
import kotlinx.coroutines.launch
import java.security.MessageDigest

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefs = getSharedPreferences("caretracker", MODE_PRIVATE)

        val savedUserId = prefs.getLong("current_user_id", -1L)
        if (savedUserId != -1L) {
            goToMain(savedUserId)
            return
        }

        binding.btnLogin.setOnClickListener { handleLogin() }
        binding.btnRegister.setOnClickListener { handleRegister() }
    }

    private fun handleLogin() {
        val username = binding.etUsername.text.toString().trim()
        val password = binding.etPassword.text.toString()

        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter username and password")
            return
        }

        lifecycleScope.launch {
            val repo = (application as CareTrackerApp).repository
            val user = repo.getUserByUsername(username)

            if (user != null && verifyPassword(password, user.passwordHash)) {
                prefs.edit().putLong("current_user_id", user.id).apply()
                goToMain(user.id)
            } else {
                showError("Invalid username or password")
            }
        }
    }

    private fun handleRegister() {
        val username = binding.etUsername.text.toString().trim()
        val password = binding.etPassword.text.toString()

        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter username and password")
            return
        }

        lifecycleScope.launch {
            val repo = (application as CareTrackerApp).repository
            val existing = repo.getUserByUsername(username)
            if (existing != null) {
                showError("Username already exists")
                return@launch
            }

            val user = UserEntity(
                username = username,
                email = "$username@local.app",
                passwordHash = PasswordHasher.hash(password),
                displayName = username
            )

            val id = repo.insertUser(user)
            prefs.edit().putLong("current_user_id", id).apply()
            goToMain(id)
        }
    }

    private fun verifyPassword(password: String, storedHash: String): Boolean {
        return if (storedHash.contains(":")) {
            PasswordHasher.verify(password, storedHash)
        } else {
            legacySha256(password) == storedHash
        }
    }

    private fun legacySha256(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    private fun showError(msg: String) {
        binding.tvError.text = msg
        binding.tvError.visibility = View.VISIBLE
    }

    private fun goToMain(userId: Long) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("user_id", userId)
        startActivity(intent)
        finish()
    }
}
