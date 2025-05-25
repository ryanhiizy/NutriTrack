package com.fit2081.a1ryanhii34466576.ui.screen.nutricoach

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fit2081.a1ryanhii34466576.data.model.Fruit
import com.fit2081.a1ryanhii34466576.data.model.NutriCoachTip
import com.fit2081.a1ryanhii34466576.data.model.Patient
import com.fit2081.a1ryanhii34466576.data.repository.FruitRepository
import com.fit2081.a1ryanhii34466576.data.repository.GenAiRepository
import com.fit2081.a1ryanhii34466576.data.repository.NutriCoachTipRepository
import com.fit2081.a1ryanhii34466576.data.repository.PatientRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NutriCoachViewModel(application: Application) : AndroidViewModel(application) {
    private val patientRepo = PatientRepository(application.applicationContext)
    private val tipRepo = NutriCoachTipRepository(application.applicationContext)
    private val genAiRepo = GenAiRepository()
    private val fruitRepo = FruitRepository()

    private val _currentPatient = MutableStateFlow<Patient?>(null)
    val currentPatient: StateFlow<Patient?> = _currentPatient.asStateFlow()

    private val _isFruitScoreOptimal = MutableStateFlow<Boolean?>(null)
    val isFruitScoreOptimal: StateFlow<Boolean?> = _isFruitScoreOptimal.asStateFlow()

    private val _motivationalMessage = MutableStateFlow<String?>(null)
    val motivationalMessage: StateFlow<String?> = _motivationalMessage.asStateFlow()

    private val _savedTips = MutableStateFlow<List<String>>(emptyList())
    val savedTips: StateFlow<List<String>> = _savedTips.asStateFlow()

    private val _fruitInput = MutableStateFlow("")
    val fruitInput: StateFlow<String> = _fruitInput.asStateFlow()

    private val _fruitInfo = MutableStateFlow<Fruit?>(null)
    val fruitInfo: StateFlow<Fruit?> = _fruitInfo.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _isLoadingTip = MutableStateFlow(false)
    val isLoadingTip: StateFlow<Boolean> = _isLoadingTip.asStateFlow()

    private val _isLoadingFruit = MutableStateFlow(false)
    val isLoadingFruit: StateFlow<Boolean> = _isLoadingFruit.asStateFlow()

    private val _imageUrl = MutableStateFlow<String?>(null)
    val imageUrl: StateFlow<String?> = _imageUrl.asStateFlow()

    private val _showTipsDialog = MutableStateFlow(false)
    val showTipsDialog: StateFlow<Boolean> = _showTipsDialog.asStateFlow()

    init {
        loadTips()
    }

    fun loadUser(userId: String) {
        viewModelScope.launch {
            val patient = patientRepo.getPatientById(userId)
            _currentPatient.value = patient

            // Check if the fruit score is optimal based on the patient's data
            val isOptimal = patient?.let {
                it.fruitServeSize >= 2.0 && it.fruitVariationsScore >= 2.0
            }
            _isFruitScoreOptimal.value = isOptimal

            // If the fruit score is optimal, load a random image
            if (isOptimal == true) {
                _imageUrl.value = "https://picsum.photos/600?random=${System.currentTimeMillis()}"
            }
        }
    }

    fun onFruitInputChange(newInput: String) {
        _fruitInput.value = newInput
    }

    fun fetchFruitInfo() {
        val fruitName = _fruitInput.value.trim().lowercase()
        if (fruitName.isEmpty() || fruitName == "all") {
            _fruitInfo.value = null
            _error.value = "Please enter a valid fruit name"
            return
        }

        viewModelScope.launch {
            _isLoadingFruit.value = true

            try {
                // Fetch fruit information from the repository
                val result = fruitRepo.getFruitByName(fruitName)
                _fruitInfo.value = result
                _error.value = null
            } catch (e: Exception) {
                _fruitInfo.value = null
                _error.value = "Fruit not found"
                Log.e("NutriCoachViewModel", "Error fetching fruit info", e)
            } finally {
                _isLoadingFruit.value = false
            }
        }
    }


    fun generateMotivationalMessage(patient: Patient) {
        val prompt = buildPrompt(patient)

        viewModelScope.launch {
            _isLoadingTip.value = true

            try {
                // Generate a motivational message using the GenAI repository
                val result = genAiRepo.prompt(prompt)
                _motivationalMessage.value = result
                result?.let {
                    // Save the generated message as a tip
                    tipRepo.insertTip(NutriCoachTip(message = it))
                    // Reload tips to include the new one
                    loadTips()
                }
            } catch (e: Exception) {
                _motivationalMessage.value = "Failed to generate message"
                Log.e("NutriCoachViewModel", "Failed to generate message", e)
            } finally {
                _isLoadingTip.value = false
            }
        }
    }

    private fun loadTips() {
        viewModelScope.launch {
            tipRepo.getAllTips().collect { tips ->
                _savedTips.value = tips.map { it.message }
            }
        }
    }

    private fun buildPrompt(patient: Patient): String {
        return """
        Generate a short, friendly, and motivational paragraph encouraging them to improve their sub-optimal scores. Include encouragement and a realistic tip they could try.

        Their scores:
        - Discretionary Score: ${patient.discretionaryScore} (Max 10)
        - Vegetable Score: ${patient.vegetableScore} (Max 10)
        - Fruit Score: ${patient.fruitScore} (Max 10)
        - Fruit Serve Size: ${patient.fruitServeSize} (>= 2 serves is optimal)
        - Fruit Variety Score: ${patient.fruitVariationsScore} (>= 2 is optimal)
        - Grains/Cereals Score: ${patient.grainsCerealsScore} (Max 5)
        - Whole Grains Score: ${patient.wholeGrainsScore} (Max 5)
        - Meat/Alternatives Score: ${patient.meatAlternativesScore} (Max 10)
        - Dairy Score: ${patient.dairyAlternativesScore} (Max 10)
        - Water Score: ${patient.waterScore} (Max 5)
        - Sodium Score: ${patient.sodiumScore} (Max 10)
        - Sugar Score: ${patient.sugarScore} (Max 10)
        - Alcohol Score: ${patient.alcoholScore} (Max 5)
        - Saturated Fat Score: ${patient.saturatedFatScore} (Max 5)
        - Unsaturated Fat Score: ${patient.unsaturatedFatScore} (Max 5)
        """.trimIndent()
    }

    fun openTipsDialog() {
        _showTipsDialog.value = true
    }

    fun closeTipsDialog() {
        _showTipsDialog.value = false
    }
}
