package com.fit2081.a1ryanhii34466576.ui.screen.login

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fit2081.a1ryanhii34466576.data.model.Patient
import com.fit2081.a1ryanhii34466576.data.preferences.SessionManager
import com.fit2081.a1ryanhii34466576.data.repository.FoodIntakeRepository
import com.fit2081.a1ryanhii34466576.data.repository.PatientRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.mindrot.jbcrypt.BCrypt

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val patientRepository = PatientRepository(application.applicationContext)
    private val intakeRepository = FoodIntakeRepository(application.applicationContext)
    private val sessionManager = SessionManager(application.applicationContext)

    private val _claimedUsers = MutableStateFlow<List<Patient>>(emptyList())
    val claimedUsers: StateFlow<List<Patient>> = _claimedUsers.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(sessionManager.isLoggedIn())
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _loggedInUserId = MutableStateFlow<String?>(sessionManager.getLoggedInUserId())
    val loggedInUserId: StateFlow<String?> = _loggedInUserId.asStateFlow()

    private val _loginNavigationTarget = MutableStateFlow<String?>(null)
    val loginNavigationTarget: StateFlow<String?> = _loginNavigationTarget.asStateFlow()

    init {
        loadClaimedUsers()
    }

    fun loadClaimedUsers() {
        viewModelScope.launch {
            try {
                // Load all patients and filter those with non-blank passwords
                patientRepository.getAllPatients().collect { patients ->
                    _claimedUsers.value = patients.filter { it.password.isNotBlank() }
                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Failed to load claimed users", e)
            }
        }
    }

    fun authenticate(userId: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val patient = patientRepository.getPatientById(userId)

                // Check if the patient exists and if the password matches
                if (patient != null && BCrypt.checkpw(password, patient.password)) {
                    // Save the user session
                    sessionManager.saveUserSession(userId)
                    _isLoggedIn.value = true
                    _loggedInUserId.value = userId

                    // Determine redirection target based on food intake
                    val intake = intakeRepository.getFoodIntake(userId)
                    _loginNavigationTarget.value = if (intake == null) {
                        "questionnaire/$userId"
                    } else {
                        "home/$userId"
                    }

                    onResult(true)
                } else {
                    onResult(false)
                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Authentication failed", e)
                onResult(false)
            }
        }
    }

    fun clearNavigationTarget() {
        _loginNavigationTarget.value = null
    }
}
