package com.fit2081.a1ryanhii34466576.data.local

import android.content.Context
import android.util.Log
import com.fit2081.a1ryanhii34466576.data.model.Patient
import java.io.BufferedReader
import java.io.InputStreamReader

object CsvReader {
    private const val PHONE_NUMBER = "PhoneNumber"
    private const val USER_ID = "User_ID"
    private const val SEX = "Sex"

    private const val FOOD_SCORE_MALE = "HEIFAtotalscoreMale"
    private const val FOOD_SCORE_FEMALE = "HEIFAtotalscoreFemale"
    private const val DISCRETIONARY_SCORE_MALE = "DiscretionaryHEIFAscoreMale"
    private const val DISCRETIONARY_SCORE_FEMALE = "DiscretionaryHEIFAscoreFemale"
    private const val VEGETABLE_SCORE_MALE = "VegetablesHEIFAscoreMale"
    private const val VEGETABLE_SCORE_FEMALE = "VegetablesHEIFAscoreFemale"
    private const val FRUIT_SCORE_MALE = "FruitHEIFAscoreMale"
    private const val FRUIT_SCORE_FEMALE = "FruitHEIFAscoreFemale"
    private const val GRAINS_CEREALS_SCORE_MALE = "GrainsandcerealsHEIFAscoreMale"
    private const val GRAINS_CEREALS_SCORE_FEMALE = "GrainsandcerealsHEIFAscoreFemale"
    private const val WHOLE_GRAINS_SCORE_MALE = "WholegrainsHEIFAscoreMale"
    private const val WHOLE_GRAINS_SCORE_FEMALE = "WholegrainsHEIFAscoreFemale"
    private const val MEAT_ALTERNATIVES_SCORE_MALE = "MeatandalternativesHEIFAscoreMale"
    private const val MEAT_ALTERNATIVES_SCORE_FEMALE = "MeatandalternativesHEIFAscoreFemale"
    private const val DAIRY_ALTERNATIVES_SCORE_MALE = "DairyandalternativesHEIFAscoreMale"
    private const val DAIRY_ALTERNATIVES_SCORE_FEMALE = "DairyandalternativesHEIFAscoreFemale"
    private const val WATER_SCORE_MALE = "WaterHEIFAscoreMale"
    private const val WATER_SCORE_FEMALE = "WaterHEIFAscoreFemale"
    private const val SODIUM_SCORE_MALE = "SodiumHEIFAscoreMale"
    private const val SODIUM_SCORE_FEMALE = "SodiumHEIFAscoreFemale"
    private const val SUGAR_SCORE_MALE = "SugarHEIFAscoreMale"
    private const val SUGAR_SCORE_FEMALE = "SugarHEIFAscoreFemale"
    private const val ALCOHOL_SCORE_MALE = "AlcoholHEIFAscoreMale"
    private const val ALCOHOL_SCORE_FEMALE = "AlcoholHEIFAscoreFemale"
    private const val SATURATED_FAT_SCORE_MALE = "SaturatedFatHEIFAscoreMale"
    private const val SATURATED_FAT_SCORE_FEMALE = "SaturatedFatHEIFAscoreFemale"
    private const val UNSATURATED_FAT_SCORE_MALE = "UnsaturatedFatHEIFAscoreMale"
    private const val UNSATURATED_FAT_SCORE_FEMALE = "UnsaturatedFatHEIFAscoreFemale"

    private const val DISCRETIONARY_SERVE_SIZE = "Discretionaryservesize"
    private const val VEGETABLES_WITH_LEGUMES_ALLOCATED_SERVE_SIZE =
        "Vegetableswithlegumesallocatedservesize"
    private const val LEGUMES_ALLOCATED_VEGETABLES = "LegumesallocatedVegetables"
    private const val VEGETABLES_VARIATIONS_SCORE = "Vegetablesvariationsscore"
    private const val VEGETABLES_CRUCIFEROUS = "VegetablesCruciferous"
    private const val VEGETABLES_TUBER_AND_BULB = "VegetablesTuberandbulb"
    private const val VEGETABLES_OTHER = "VegetablesOther"
    private const val LEGUMES = "Legumes"
    private const val VEGETABLES_GREEN = "VegetablesGreen"
    private const val VEGETABLES_RED_AND_ORANGE = "VegetablesRedandorange"
    private const val FRUIT_SERVE_SIZE = "Fruitservesize"
    private const val FRUIT_VARIATIONS_SCORE = "Fruitvariationsscore"
    private const val FRUIT_POME = "FruitPome"
    private const val FRUIT_TROPICAL_AND_SUBTROPICAL = "FruitTropicalandsubtropical"
    private const val FRUIT_BERRY = "FruitBerry"
    private const val FRUIT_STONE = "FruitStone"
    private const val FRUIT_CITRUS = "FruitCitrus"
    private const val FRUIT_OTHER = "FruitOther"
    private const val GRAINS_CEREALS_SERVE_SIZE = "Grainsandcerealsservesize"
    private const val GRAINS_CEREALS_NON_WHOLE_GRAINS = "GrainsandcerealsNonwholegrains"
    private const val WHOLE_GRAINS_SERVE_SIZE = "Wholegrainsservesize"
    private const val MEAT_ALTERNATIVES_SERVE_SIZE =
        "Meatandalternativeswithlegumesallocatedservesize"
    private const val LEGUMES_ALLOCATED_MEAT = "LegumesallocatedMeatandalternatives"
    private const val DAIRY_SERVE_SIZE = "Dairyandalternativesservesize"
    private const val SODIUM_MG = "Sodiummgmilligrams"
    private const val ALCOHOL_STANDARD_DRINKS = "Alcoholstandarddrinks"
    private const val WATER = "Water"
    private const val WATER_TOTAL_ML = "WaterTotalmL"
    private const val BEVERAGE_TOTAL_ML = "BeverageTotalmL"
    private const val SUGAR = "Sugar"
    private const val SATURATED_FAT = "SaturatedFat"
    private const val UNSATURATED_FAT_SERVE_SIZE = "UnsaturatedFatservesize"


    fun loadUserData(context: Context): List<Patient> {
        val patients = mutableListOf<Patient>()

        try {
            // Open the CSV file from assets
            val inputStream = context.assets.open("UserData.csv")
            val reader = BufferedReader(InputStreamReader(inputStream))

            // Read the header line to get column names
            val headers = reader.readLine().split(",").map { it.trim() }

            // Create a map of column names to their indices for easy access
            val columnIndices = headers.withIndex().associate { it.value to it.index }

            reader.forEachLine { line ->
                val tokens = line.split(",").map { it.trim() }

                fun getString(columnName: String): String =
                    tokens.getOrNull(columnIndices[columnName] ?: -1) ?: ""

                val sex = getString(SEX)

                fun getScore(columnName: String): Double =
                    tokens.getOrNull(columnIndices[columnName] ?: -1)?.toDoubleOrNull() ?: 0.0

                // Get appropriate score based on sex
                fun getScoreBasedOnGender(maleColumn: String, femaleColumn: String): Double =
                    getScore(if (sex == "Male") maleColumn else femaleColumn)

                patients.add(
                    Patient(
                        userId = getString(USER_ID),
                        phoneNumber = getString(PHONE_NUMBER),
                        sex = sex,
                        foodScore =
                            getScoreBasedOnGender(
                                FOOD_SCORE_MALE,
                                FOOD_SCORE_FEMALE
                            ),
                        discretionaryScore =
                            getScoreBasedOnGender(
                                DISCRETIONARY_SCORE_MALE,
                                DISCRETIONARY_SCORE_FEMALE
                            ),
                        vegetableScore =
                            getScoreBasedOnGender(
                                VEGETABLE_SCORE_MALE,
                                VEGETABLE_SCORE_FEMALE
                            ),
                        fruitScore =
                            getScoreBasedOnGender(
                                FRUIT_SCORE_MALE,
                                FRUIT_SCORE_FEMALE
                            ),
                        grainsCerealsScore =
                            getScoreBasedOnGender(
                                GRAINS_CEREALS_SCORE_MALE,
                                GRAINS_CEREALS_SCORE_FEMALE
                            ),
                        wholeGrainsScore =
                            getScoreBasedOnGender(
                                WHOLE_GRAINS_SCORE_MALE,
                                WHOLE_GRAINS_SCORE_FEMALE
                            ),
                        meatAlternativesScore =
                            getScoreBasedOnGender(
                                MEAT_ALTERNATIVES_SCORE_MALE,
                                MEAT_ALTERNATIVES_SCORE_FEMALE
                            ),
                        dairyAlternativesScore =
                            getScoreBasedOnGender(
                                DAIRY_ALTERNATIVES_SCORE_MALE,
                                DAIRY_ALTERNATIVES_SCORE_FEMALE
                            ),
                        waterScore =
                            getScoreBasedOnGender(
                                WATER_SCORE_MALE,
                                WATER_SCORE_FEMALE
                            ),
                        sodiumScore =
                            getScoreBasedOnGender(
                                SODIUM_SCORE_MALE,
                                SODIUM_SCORE_FEMALE
                            ),
                        sugarScore =
                            getScoreBasedOnGender(
                                SUGAR_SCORE_MALE,
                                SUGAR_SCORE_FEMALE
                            ),
                        alcoholScore =
                            getScoreBasedOnGender(
                                ALCOHOL_SCORE_MALE,
                                ALCOHOL_SCORE_FEMALE
                            ),
                        saturatedFatScore =
                            getScoreBasedOnGender(
                                SATURATED_FAT_SCORE_MALE,
                                SATURATED_FAT_SCORE_FEMALE
                            ),
                        unsaturatedFatScore =
                            getScoreBasedOnGender(
                                UNSATURATED_FAT_SCORE_MALE,
                                UNSATURATED_FAT_SCORE_FEMALE
                            ),
                        discretionaryServeSize = getScore(DISCRETIONARY_SERVE_SIZE),
                        vegetablesWithLegumesAllocatedServeSize = getScore(
                            VEGETABLES_WITH_LEGUMES_ALLOCATED_SERVE_SIZE
                        ),
                        legumesAllocatedVegetables = getScore(LEGUMES_ALLOCATED_VEGETABLES),
                        vegetablesVariationsScore = getScore(VEGETABLES_VARIATIONS_SCORE),
                        vegetablesCruciferous = getScore(VEGETABLES_CRUCIFEROUS),
                        vegetablesTuberAndBulb = getScore(VEGETABLES_TUBER_AND_BULB),
                        vegetablesOther = getScore(VEGETABLES_OTHER),
                        legumes = getScore(LEGUMES),
                        vegetablesGreen = getScore(VEGETABLES_GREEN),
                        vegetablesRedAndOrange = getScore(VEGETABLES_RED_AND_ORANGE),
                        fruitServeSize = getScore(FRUIT_SERVE_SIZE),
                        fruitVariationsScore = getScore(FRUIT_VARIATIONS_SCORE),
                        fruitPome = getScore(FRUIT_POME),
                        fruitTropicalAndSubtropical = getScore(FRUIT_TROPICAL_AND_SUBTROPICAL),
                        fruitBerry = getScore(FRUIT_BERRY),
                        fruitStone = getScore(FRUIT_STONE),
                        fruitCitrus = getScore(FRUIT_CITRUS),
                        fruitOther = getScore(FRUIT_OTHER),
                        grainsCerealsServeSize = getScore(GRAINS_CEREALS_SERVE_SIZE),
                        grainsCerealsNonWholeGrains = getScore(GRAINS_CEREALS_NON_WHOLE_GRAINS),
                        wholeGrainsServeSize = getScore(WHOLE_GRAINS_SERVE_SIZE),
                        meatAlternativesWithLegumesAllocatedServeSize = getScore(
                            MEAT_ALTERNATIVES_SERVE_SIZE
                        ),
                        legumesAllocatedMeatAlternatives = getScore(LEGUMES_ALLOCATED_MEAT),
                        dairyAlternativesServeSize = getScore(DAIRY_SERVE_SIZE),
                        sodiumMgMilligrams = getScore(SODIUM_MG),
                        alcoholStandardDrinks = getScore(ALCOHOL_STANDARD_DRINKS),
                        water = getScore(WATER),
                        waterTotalMl = getScore(WATER_TOTAL_ML),
                        beverageTotalMl = getScore(BEVERAGE_TOTAL_ML),
                        sugar = getScore(SUGAR),
                        saturatedFat = getScore(SATURATED_FAT),
                        unsaturatedFatServeSize = getScore(UNSATURATED_FAT_SERVE_SIZE),
                    )
                )
            }
            reader.close()
        } catch (e: Exception) {
            Log.e("CsvReader", "Error reading CSV file", e)
        }
        return patients
    }
}