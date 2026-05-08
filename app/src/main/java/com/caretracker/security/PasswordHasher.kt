package com.caretracker.security

import android.util.Base64
import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

object PasswordHasher {
    private const val ITERATIONS = 120_000
    private const val KEY_LENGTH = 256
    private const val ALGORITHM = "PBKDF2WithHmacSHA256"

    fun hash(password: String): String {
        val salt = ByteArray(16)
        SecureRandom().nextBytes(salt)

        val hash = pbkdf2(password, salt, ITERATIONS, KEY_LENGTH)
        val saltB64 = Base64.encodeToString(salt, Base64.NO_WRAP)
        val hashB64 = Base64.encodeToString(hash, Base64.NO_WRAP)
        return "$ITERATIONS:$saltB64:$hashB64"
    }

    fun verify(password: String, storedValue: String): Boolean {
        val parts = storedValue.split(":")
        if (parts.size != 3) return false

        val iterations = parts[0].toIntOrNull() ?: return false
        val salt = Base64.decode(parts[1], Base64.NO_WRAP)
        val expectedHash = Base64.decode(parts[2], Base64.NO_WRAP)
        val actualHash = pbkdf2(password, salt, iterations, expectedHash.size * 8)

        return actualHash.contentEquals(expectedHash)
    }

    private fun pbkdf2(password: String, salt: ByteArray, iterations: Int, keyLength: Int): ByteArray {
        val spec = PBEKeySpec(password.toCharArray(), salt, iterations, keyLength)
        val factory = SecretKeyFactory.getInstance(ALGORITHM)
        return factory.generateSecret(spec).encoded
    }
}
