package com.fit2081.a1ryanhii34466576.util

import java.security.MessageDigest

object PasswordUtil {
    fun hashPassword(password: String): String {
        // Convert the password to bytes
        val bytes = password.toByteArray()
        // Create a SHA-256 hash of the password
        val digest = MessageDigest.getInstance("SHA-256").digest(bytes)
        // Convert the byte array to a hexadecimal string
        return digest.joinToString("") { "%02x".format(it) }
    }

    fun isPasswordValid(password: String, storedHash: String): Boolean {
        return hashPassword(password) == storedHash
    }

    fun validate(password: String, confirmPassword: String): String? {
        return when {
            password.isBlank() ->
                "Password cannot be empty"

            password != confirmPassword ->
                "Passwords do not match"

            password.length < 8 ->
                "Password must be at least 8 characters"

            !password.any { it.isUpperCase() } ->
                "Password must contain an uppercase letter"

            !password.any { it.isLowerCase() } ->
                "Password must contain a lowercase letter"

            !password.any { it.isDigit() } ->
                "Password must contain a digit"

            else -> null
        }
    }
}
