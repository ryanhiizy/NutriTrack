package com.fit2081.a1ryanhii34466576.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "food_intakes",
    foreignKeys =
        [ForeignKey(
            entity = Patient::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )]
)
data class FoodIntake(
    @PrimaryKey val userId: String,
    val selectedCategories: String, // comma-separated list
    val persona: String,
    val mealTime: String,
    val sleepTime: String,
    val wakeUpTime: String
)
