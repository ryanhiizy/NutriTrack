package com.fit2081.a1ryanhii34466576.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "nutricoach_tips")
data class NutriCoachTip(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val message: String
)
