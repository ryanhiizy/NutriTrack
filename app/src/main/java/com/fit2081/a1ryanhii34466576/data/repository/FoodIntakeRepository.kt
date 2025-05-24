package com.fit2081.a1ryanhii34466576.data.repository

import android.content.Context
import com.fit2081.a1ryanhii34466576.data.local.NutriTrackDatabase
import com.fit2081.a1ryanhii34466576.data.model.FoodIntake
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FoodIntakeRepository(context: Context) {
    private val foodIntakeDao = NutriTrackDatabase.getDatabase(context).foodIntakeDao()

    suspend fun insertFoodIntake(foodIntake: FoodIntake) =
        withContext(Dispatchers.IO) { foodIntakeDao.insertFoodIntake(foodIntake) }

    suspend fun getFoodIntake(userId: String): FoodIntake? =
        withContext(Dispatchers.IO) { foodIntakeDao.getFoodIntakeById(userId) }
}
