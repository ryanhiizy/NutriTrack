package com.fit2081.a1ryanhii34466576.ui.screen.home

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

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = PatientRepository(application.applicationContext)

    private val _patient = MutableStateFlow<Patient?>(null)
    val patient: StateFlow<Patient?> = _patient.asStateFlow()

    fun loadPatient(userId: String) {
        viewModelScope.launch {
            try {
                _patient.value = repository.getPatientById(userId)
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error loading patient", e)
            }
        }
    }
}
