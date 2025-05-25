package com.fit2081.a1ryanhii34466576.ui.screen.questionnaire

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fit2081.a1ryanhii34466576.data.model.FoodIntake
import com.fit2081.a1ryanhii34466576.data.repository.FoodIntakeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QuestionnaireViewModel : ViewModel() {
    private val _selectedCategories = MutableStateFlow<Set<String>>(emptySet())
    val selectedCategories: StateFlow<Set<String>> = _selectedCategories.asStateFlow()

    private val _dropdownSelectedPersona = MutableStateFlow("")
    val dropdownSelectedPersona: StateFlow<String> = _dropdownSelectedPersona.asStateFlow()

    private val _biggestMealTime = MutableStateFlow("")
    val biggestMealTime: StateFlow<String> = _biggestMealTime.asStateFlow()

    private val _sleepTime = MutableStateFlow("")
    val sleepTime: StateFlow<String> = _sleepTime.asStateFlow()

    private val _wakeUpTime = MutableStateFlow("")
    val wakeUpTime: StateFlow<String> = _wakeUpTime.asStateFlow()

    private val _showModal = MutableStateFlow(false)
    val showModal: StateFlow<Boolean> = _showModal.asStateFlow()

    private val _selectedPersona = MutableStateFlow("")
    val selectedPersona: StateFlow<String> = _selectedPersona.asStateFlow()

    private val _personaDescription = MutableStateFlow("")
    val personaDescription: StateFlow<String> = _personaDescription.asStateFlow()

    private val _dropdownExpanded = MutableStateFlow(false)
    val dropdownExpanded: StateFlow<Boolean> = _dropdownExpanded.asStateFlow()

    fun toggleCategory(category: String) {
        _selectedCategories.value = _selectedCategories.value.toMutableSet().apply {
            if (contains(category)) remove(category) else add(category)
        }
    }

    fun selectPersona(persona: String, description: String) {
        _selectedPersona.value = persona
        _personaDescription.value = description
        _showModal.value = true
    }

    fun dismissModal() {
        _showModal.value = false
    }

    fun setDropdownPersona(persona: String) {
        _dropdownSelectedPersona.value = persona
        _dropdownExpanded.value = false
    }

    fun setTime(field: String, value: String) {
        when (field) {
            "meal" -> _biggestMealTime.value = value
            "sleep" -> _sleepTime.value = value
            "wake" -> _wakeUpTime.value = value
        }
    }

    fun toggleDropdown() {
        _dropdownExpanded.value = !_dropdownExpanded.value
    }

    fun loadFromRepository(context: Context, userId: String) {
        val repo = FoodIntakeRepository(context)

        viewModelScope.launch {
            try {
                // Attempt to load the latest food intake for the user
                val existing = repo.getLatestFoodIntake(userId)
                existing?.let { loadFromIntake(it) }
            } catch (e: Exception) {
                Log.e("QuestionnaireViewModel", "Failed to load intake", e)
            }
        }
    }

    private fun loadFromIntake(intake: FoodIntake) {
        _selectedCategories.value = intake.selectedCategories.split(",").toSet()
        _dropdownSelectedPersona.value = intake.persona
        _biggestMealTime.value = intake.mealTime
        _sleepTime.value = intake.sleepTime
        _wakeUpTime.value = intake.wakeUpTime
    }

    private fun hasTimeConflict(): Boolean {
        // Check if meal, sleep, and wake times are the same
        return setOf(_biggestMealTime.value, _sleepTime.value, _wakeUpTime.value).size < 3
    }

    fun validateInputs(): String? {
        return when {
            _selectedCategories.value.isEmpty() ||
                    _dropdownSelectedPersona.value.isEmpty() ||
                    _biggestMealTime.value.isEmpty() ||
                    _sleepTime.value.isEmpty() ||
                    _wakeUpTime.value.isEmpty() -> "Please fill all fields"

            hasTimeConflict() -> "Meal, sleep, and wake times must be different"
            else -> null
        }
    }

    fun saveIntake(
        context: Context,
        userId: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val error = validateInputs()
        if (error != null) {
            onError(error)
            return
        }

        // Create a new FoodIntake object with the current state
        val repo = FoodIntakeRepository(context)
        val foodIntake = FoodIntake(
            userId = userId,
            selectedCategories = _selectedCategories.value.joinToString(","),
            persona = _dropdownSelectedPersona.value,
            mealTime = _biggestMealTime.value,
            sleepTime = _sleepTime.value,
            wakeUpTime = _wakeUpTime.value
        )

        viewModelScope.launch {
            try {
                val latest = repo.getLatestFoodIntake(userId)

                // If no previous food intake exists, insert the new one
                // If there is already a food intake, 
                // only insert if the new one is different
                if (latest == null ||
                    (latest.selectedCategories != foodIntake.selectedCategories ||
                            latest.persona != foodIntake.persona ||
                            latest.mealTime != foodIntake.mealTime ||
                            latest.sleepTime != foodIntake.sleepTime ||
                            latest.wakeUpTime != foodIntake.wakeUpTime)
                ) {
                    repo.insertFoodIntake(foodIntake)
                }

                onSuccess()
            } catch (e: Exception) {
                Log.e("QuestionnaireViewModel", "Failed to save food intake", e)
                onError("Failed to save. Try again.")
            }
        }
    }
}
