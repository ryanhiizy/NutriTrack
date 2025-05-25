package com.fit2081.a1ryanhii34466576.ui.screen.settings

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fit2081.a1ryanhii34466576.data.preferences.SessionManager
import com.fit2081.a1ryanhii34466576.data.repository.PatientRepository
import com.fit2081.a1ryanhii34466576.util.PasswordUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Represents the fields that can be edited in the settings screen
enum class EditField { NAME, PHONE, PASSWORD }

class SettingsViewModel : ViewModel() {
    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber: StateFlow<String> = _phoneNumber.asStateFlow()

    private val _editableName = MutableStateFlow("")
    val editableName: StateFlow<String> = _editableName.asStateFlow()

    private val _editablePhone = MutableStateFlow("")
    val editablePhone: StateFlow<String> = _editablePhone.asStateFlow()

    private val _editablePassword = MutableStateFlow("")
    val editablePassword: StateFlow<String> = _editablePassword.asStateFlow()

    private val _showPassword = MutableStateFlow(false)
    val showPassword: StateFlow<Boolean> = _showPassword.asStateFlow()

    private val _activeEditField = MutableStateFlow<EditField?>(null)
    val activeEditField: StateFlow<EditField?> = _activeEditField.asStateFlow()

    private val _editError = MutableStateFlow<String?>(null)
    val editError: StateFlow<String?> = _editError.asStateFlow()

    private var currentUserId: String? = null

    fun loadUser(context: Context, userId: String) {
        currentUserId = userId
        val repository = PatientRepository(context)

        viewModelScope.launch {
            try {
                // Load user data from repository
                val patient = repository.getPatientById(userId)

                // Update state flows with user data
                if (patient != null) {
                    _userName.value = patient.name
                    _phoneNumber.value = patient.phoneNumber
                }
            } catch (e: Exception) {
                Log.e("SettingsViewModel", "Error loading user data", e)
            }
        }
    }

    fun openFieldEditDialog(field: EditField) {
        _activeEditField.value = field
        _editError.value = null

        // Reset editable fields based on the field being edited
        when (field) {
            EditField.NAME -> _editableName.value = _userName.value
            EditField.PHONE -> _editablePhone.value = _phoneNumber.value
            EditField.PASSWORD -> _editablePassword.value = ""
        }
    }

    fun closeFieldEditDialog() {
        _activeEditField.value = null
    }

    fun togglePasswordVisibility() {
        _showPassword.value = !_showPassword.value
    }

    fun onEditableNameChange(newName: String) {
        _editableName.value = newName
    }

    fun onEditablePhoneChange(newPhone: String) {
        if (newPhone.all { it.isDigit() }) {
            _editablePhone.value = newPhone
        }
    }

    fun onEditablePasswordChange(newPassword: String) {
        _editablePassword.value = newPassword
    }

    fun saveSingleField(context: Context, field: EditField, onError: (String) -> Unit) {
        val userId = currentUserId
        if (userId == null) {
            onError("User ID is not set.")
            return
        }

        val repository = PatientRepository(context)

        viewModelScope.launch {
            try {
                // Fetch the current user from the repository
                val current = repository.getPatientById(userId)

                if (current != null) {
                    val updated = when (field) {
                        // Handle name edit
                        EditField.NAME -> {
                            if (_editableName.value.isBlank()) {
                                _editError.value = "Name cannot be empty"
                                return@launch
                            }
                            current.copy(name = _editableName.value).also {
                                _userName.value = _editableName.value
                            }
                        }

                        // Handle phone edit
                        EditField.PHONE -> {
                            val phone = _editablePhone.value
                            if (phone.isBlank()) {
                                _editError.value = "Phone number cannot be empty"
                                return@launch
                            }
                            if (!phone.all { it.isDigit() }) {
                                _editError.value = "Phone number must contain digits only"
                                return@launch
                            }
                            current.copy(phoneNumber = phone).also {
                                _phoneNumber.value = phone
                            }
                        }

                        // Handle password edit
                        EditField.PASSWORD -> {
                            val password = _editablePassword.value
                            PasswordUtil.validate(password, password)?.let {
                                _editError.value = it
                                return@launch
                            }
                            val hashed = PasswordUtil.hashPassword(password)
                            current.copy(password = hashed)
                        }
                    }

                    repository.updatePatient(updated)
                    closeFieldEditDialog()
                } else {
                    onError("User not found.")
                }
            } catch (e: Exception) {
                Log.e("SettingsViewModel", "Failed to update user", e)
                onError("Failed to update user.")
            }
        }
    }

    fun logout(context: Context, onLogoutComplete: () -> Unit) {
        try {
            // Clear session data
            SessionManager(context).clearSession()
            onLogoutComplete()
        } catch (e: Exception) {
            Log.e("SettingsViewModel", "Logout failed", e)
        }
    }
}
