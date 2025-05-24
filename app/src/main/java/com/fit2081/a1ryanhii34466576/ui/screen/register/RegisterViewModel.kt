package com.fit2081.a1ryanhii34466576.ui.screen.register

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fit2081.a1ryanhii34466576.data.model.Patient
import com.fit2081.a1ryanhii34466576.data.repository.PatientRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.mindrot.jbcrypt.BCrypt

class RegisterViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = PatientRepository(application.applicationContext)

    private val _unclaimedUsers = MutableStateFlow<List<Patient>>(emptyList())
    val unclaimedUsers: StateFlow<List<Patient>> = _unclaimedUsers.asStateFlow()

    fun registerUser(
        userId: String,
        name: String,
        phoneNumber: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val existing = repository.getPatientById(userId)
                when {
                    existing == null -> onError("User ID not found in system.")
                    existing.password.isNotEmpty() -> onError("User ID already registered.")
                    else -> {
                        // Hash the password using BCrypt
                        val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt())

                        // Update the existing patient record with the new details
                        val updated = existing.copy(
                            name = name,
                            phoneNumber = phoneNumber,
                            password = hashedPassword
                        )

                        // Insert the updated patient record
                        repository.insertPatient(updated)
                        onSuccess()
                    }
                }
            } catch (e: Exception) {
                onError("Registration failed. Please try again.")
                Log.e("RegisterViewModel", "registerUser failed", e)
            }
        }
    }

    fun loadUnclaimedUsers() {
        viewModelScope.launch {
            try {
                // Fetch all patients and filter for unclaimed users (those with empty passwords)
                repository.getAllPatients().collect { patients ->
                    _unclaimedUsers.value = patients.filter { it.password.isEmpty() }
                }
            } catch (e: Exception) {
                Log.e("RegisterViewModel", "Failed to load unclaimed users", e)
            }
        }
    }
}
