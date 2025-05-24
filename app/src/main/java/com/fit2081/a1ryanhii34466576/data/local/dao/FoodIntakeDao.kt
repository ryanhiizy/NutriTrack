package com.fit2081.a1ryanhii34466576.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.fit2081.a1ryanhii34466576.data.model.FoodIntake
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodIntakeDao {
    @Insert
    suspend fun insertFoodIntake(foodIntake: FoodIntake)

    @Query("SELECT * FROM food_intakes WHERE userId = :userId ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatestFoodIntake(userId: String): FoodIntake?

    @Query("SELECT * FROM food_intakes WHERE userId = :userId ORDER BY timestamp DESC")
    fun getAllFoodIntakes(userId: String): Flow<List<FoodIntake>>
}
