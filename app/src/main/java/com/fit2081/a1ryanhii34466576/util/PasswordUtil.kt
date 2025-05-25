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
}
