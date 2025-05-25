package com.fit2081.a1ryanhii34466576.data.model

data class Nutrition(
    val calories: Double,
    val fat: Double,
    val sugar: Double,
    val carbohydrates: Double,
    val protein: Double
)

data class Fruit(
    val id: Int,
    val name: String,
    val family: String,
    val order: String,
    val genus: String,
    val nutritions: Nutrition
)