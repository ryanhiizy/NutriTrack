package com.fit2081.a1ryanhii34466576.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fit2081.a1ryanhii34466576.data.model.FoodIntake

@Dao
interface FoodIntakeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoodIntake(foodIntake: FoodIntake)

    @Query("SELECT * FROM food_intakes WHERE userId = :userId")
    suspend fun getFoodIntakeById(userId: String): FoodIntake?
}
