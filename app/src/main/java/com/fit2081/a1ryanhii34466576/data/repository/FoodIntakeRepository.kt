package com.fit2081.a1ryanhii34466576.data.repository

import android.content.Context
import com.fit2081.a1ryanhii34466576.data.local.NutriTrackDatabase
import com.fit2081.a1ryanhii34466576.data.model.FoodIntake
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class FoodIntakeRepository(context: Context) {
    private val foodIntakeDao = NutriTrackDatabase.getDatabase(context).foodIntakeDao()

    suspend fun insertFoodIntake(foodIntake: FoodIntake) =
        withContext(Dispatchers.IO) { foodIntakeDao.insertFoodIntake(foodIntake) }

    suspend fun getLatestFoodIntake(userId: String): FoodIntake? =
        withContext(Dispatchers.IO) { foodIntakeDao.getLatestFoodIntake(userId) }

    fun getAllFoodIntakes(userId: String): Flow<List<FoodIntake>> =
        foodIntakeDao.getAllFoodIntakes(userId)
}
