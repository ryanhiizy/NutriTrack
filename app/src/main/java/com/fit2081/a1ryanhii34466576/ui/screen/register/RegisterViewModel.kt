package com.fit2081.a1ryanhii34466576.ui.screen.register

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fit2081.a1ryanhii34466576.data.model.Patient
import com.fit2081.a1ryanhii34466576.data.repository.PatientRepository
import com.fit2081.a1ryanhii34466576.util.PasswordUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = PatientRepository(application.applicationContext)

    private val _unclaimedUsers = MutableStateFlow<List<Patient>>(emptyList())
    val unclaimedUsers: StateFlow<List<Patient>> = _unclaimedUsers.asStateFlow()

    private val _selectedUserId = MutableStateFlow("")
    val selectedUserId: StateFlow<String> = _selectedUserId.asStateFlow()

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name.asStateFlow()

    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber: StateFlow<String> = _phoneNumber.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword: StateFlow<String> = _confirmPassword.asStateFlow()

    private val _showPassword = MutableStateFlow(false)
    val showPassword: StateFlow<Boolean> = _showPassword.asStateFlow()

    private val _expanded = MutableStateFlow(false)
    val expanded: StateFlow<Boolean> = _expanded.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun onUserIdSelected(id: String) {
        _selectedUserId.value = id
        _expanded.value = false
    }

    fun onNameChange(newName: String) {
        _name.value = newName
    }

    fun onPhoneChange(newPhone: String) {
        if (newPhone.all { it.isDigit() }) {
            _phoneNumber.value = newPhone
        }
    }

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
    }

    fun onConfirmPasswordChange(newPassword: String) {
        _confirmPassword.value = newPassword
    }

    fun togglePasswordVisibility() {
        _showPassword.value = !_showPassword.value
    }

    fun toggleDropdown() {
        _expanded.value = !_expanded.value
    }

    fun setError(message: String?) {
        _errorMessage.value = message
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

    fun registerUserIfValid(onSuccess: () -> Unit) {
        val password = _password.value
        val error = when {
            _selectedUserId.value.isBlank() ||
                    _name.value.isBlank() ||
                    _phoneNumber.value.isBlank() ||
                    password.isBlank() ||
                    _confirmPassword.value.isBlank() ->
                "Please fill all fields"

            // .validate() returns an error message if validation fails
            PasswordUtil.validate(password, _confirmPassword.value) != null ->
                PasswordUtil.validate(password, _confirmPassword.value)

            else -> null
        }

        // Proceed with registration if no error
        if (error != null) {
            setError(error)
        } else {
            registerUser(
                _selectedUserId.value,
                _name.value,
                _phoneNumber.value,
                _password.value,
                onSuccess = onSuccess,
                onError = { setError(it) }
            )
        }
    }

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
                        // Hash the password
                        val hashedPassword = PasswordUtil.hashPassword(password)

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
}
