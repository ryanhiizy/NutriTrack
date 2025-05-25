package com.fit2081.a1ryanhii34466576.ui.screen.clinicianLogin

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ClinicianLoginViewModel : ViewModel() {
    companion object {
        private const val CLINICIAN_KEY = "dollar-entry-apples"
    }

    private val _keyInput = MutableStateFlow("")
    val keyInput: StateFlow<String> = _keyInput.asStateFlow()

    private val _errorState = MutableStateFlow(false)
    val errorState: StateFlow<Boolean> = _errorState.asStateFlow()

    fun onKeyChange(newKey: String) {
        _keyInput.value = newKey
        _errorState.value = false
    }

    fun attemptLogin(onSuccess: () -> Unit, onFailure: () -> Unit) {
        if (_keyInput.value.trim() == CLINICIAN_KEY) {
            onSuccess()
        } else {
            _errorState.value = true
            onFailure()
        }
    }
}
