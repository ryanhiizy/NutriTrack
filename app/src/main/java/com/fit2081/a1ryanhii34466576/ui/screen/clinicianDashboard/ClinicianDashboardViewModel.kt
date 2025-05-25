package com.fit2081.a1ryanhii34466576.ui.screen.clinicianDashboard

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fit2081.a1ryanhii34466576.data.model.Patient
import com.fit2081.a1ryanhii34466576.data.repository.GenAiRepository
import com.fit2081.a1ryanhii34466576.data.repository.PatientRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ClinicianDashboardViewModel(application: Application) : AndroidViewModel(application) {
    private val patientRepository = PatientRepository(application.applicationContext)
    private val genAiRepository = GenAiRepository()

    private val _patients = MutableStateFlow<List<Patient>>(emptyList())
    val patients: StateFlow<List<Patient>> = _patients.asStateFlow()

    private val _averageMaleScore = MutableStateFlow<Double?>(null)
    val averageMaleScore: StateFlow<Double?> = _averageMaleScore.asStateFlow()

    private val _averageFemaleScore = MutableStateFlow<Double?>(null)
    val averageFemaleScore: StateFlow<Double?> = _averageFemaleScore.asStateFlow()

    private val _insightList = MutableStateFlow<List<Pair<String, String>>>(emptyList())
    val insightList: StateFlow<List<Pair<String, String>>> = _insightList.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    init {
        loadPatientsAndScores()
    }

    private fun loadPatientsAndScores() {
        viewModelScope.launch {
            try {
                // Get all patients and calculate average scores based on sex
                patientRepository.getAllPatients().collect { list ->
                    _patients.value = list
                    _averageMaleScore.value =
                        list.filter { it.sex.lowercase() == "male" }.map { it.foodScore }
                            .averageOrNull()
                    _averageFemaleScore.value =
                        list.filter { it.sex.lowercase() == "female" }.map { it.foodScore }
                            .averageOrNull()
                }
            } catch (e: Exception) {
                Log.e("ClinicianDashboardViewModel", "Failed to load patients and scores", e)
            }
        }
    }

    fun generateInsightFromPatients() {
        viewModelScope.launch {
            _loading.value = true
            val prompt = buildPromptFromPatients(_patients.value)

            try {
                // Call the GenAI repository to generate insights
                val raw = genAiRepository.prompt(prompt)
                // Parse the raw response into a list of insights
                _insightList.value = parseInsights(raw ?: "")
            } catch (e: Exception) {
                Log.e("ClinicianDashboardViewModel", "Failed to generate insights", e)
                _insightList.value = listOf("Error" to "Failed to generate insights.")
            } finally {
                _loading.value = false
            }
        }
    }

    private fun parseInsights(raw: String): List<Pair<String, String>> {
        return raw.lines()
            // Remove empty lines and trim whitespace
            .map { it.trim() }
            // Split lines by the first occurrence of "|"
            .filter { it.contains("|") }
            // Map to pairs of (title, content) if the line is valid
            .mapNotNull { line ->
                val parts = line.split("|", limit = 2)
                if (parts.size == 2) parts[0].trim() to parts[1].trim() else null
            }
    }

    private fun buildPromptFromPatients(patients: List<Patient>): String {
        val sample = patients.take(50) // Limit to 50 patients to avoid overloading the prompt

        val tableData = sample.joinToString("\n") { it.toString() }

        return """
        You are a nutrition analyst. Given this patient dataset, identify 3 interesting patterns or correlations and describe them clearly.
        Do not provide any content mentioning there is no correlation or insight.

        Output format:
        Title1 | Content1  
        Title2 | Content2  
        Title3 | Content3  

        Data:
        $tableData
        """.trimIndent()
    }

    private fun List<Double>.averageOrNull(): Double? =
        if (isEmpty()) null else average()
}
