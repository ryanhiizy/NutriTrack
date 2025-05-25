package com.fit2081.a1ryanhii34466576.ui.screen.foodIntakeHistory

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fit2081.a1ryanhii34466576.data.model.FoodIntake
import com.fit2081.a1ryanhii34466576.data.repository.FoodIntakeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FoodIntakeHistoryViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = FoodIntakeRepository(application)

    // MutableStateFlow to hold the list of food intakes
    private val _foodIntakes = MutableStateFlow<List<FoodIntake>>(emptyList())
    val foodIntakes: StateFlow<List<FoodIntake>> = _foodIntakes

    fun loadFoodIntakes(userId: String) {
        viewModelScope.launch {
            try {
                // Collect the food intakes from the repository
                repository.getAllFoodIntakes(userId).collectLatest {
                    _foodIntakes.value = it
                }
            } catch (e: Exception) {
                Log.e("FoodIntakeHistoryViewModel", "Error loading food intakes", e)
            }
        }
    }

    fun deleteFoodIntake(foodIntake: FoodIntake) {
        viewModelScope.launch {
            try {
                // Call the repository to delete the food intake
                repository.deleteFoodIntake(foodIntake)
            } catch (e: Exception) {
                Log.e("FoodIntakeHistoryViewModel", "Error deleting food intake", e)
            }
        }
    }
}
