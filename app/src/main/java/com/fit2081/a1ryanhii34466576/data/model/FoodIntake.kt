package com.fit2081.a1ryanhii34466576.data.model

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "food_intakes",
    primaryKeys = ["userId", "timestamp"],
    foreignKeys = [
        ForeignKey(
            entity = Patient::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class FoodIntake(
    // Composite primary key to keep track of multiple entries for the same user
    val userId: String,
    val timestamp: Long = System.currentTimeMillis(),

    val selectedCategories: String,
    val persona: String,
    val mealTime: String,
    val sleepTime: String,
    val wakeUpTime: String
)
