package com.fit2081.a1ryanhii34466576

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader

data class User(
    val phoneNumber: String,
    val userId: String,
    val sex: String,
    val foodScore: Double,
    val discretionaryScore: Double,
    val vegetableScore: Double,
    val fruitScore: Double,
    val grainsCerealsScore: Double,
    val wholeGrainsScore: Double,
    val meatAlternativesScore: Double,
    val dairyScore: Double,
    val waterScore: Double,
    val sodiumScore: Double,
    val sugarScore: Double,
    val alcoholScore: Double,
    val saturatedFatScore: Double,
    val unsaturatedFatScore: Double
)

object CsvReader {
    fun loadUserData(context: Context): List<User> {
        val userList = mutableListOf<User>()

        try {
            val inputStream = context.assets.open("UserData.csv")
            val reader = BufferedReader(InputStreamReader(inputStream))

            reader.readLine() // Skip header

            reader.forEachLine { line ->
                val tokens = line.split(",").map { it.trim() }

                val phoneNumber = tokens[0]
                val userId = tokens[1]
                val sex = tokens[2]

                val maleIndex = if (sex == "Male") 0 else 1

                fun getScoreIndex(baseIndex: Int) = baseIndex + maleIndex

                val foodScore = tokens[getScoreIndex(3)].toDouble()
                val discretionaryScore = tokens[getScoreIndex(5)].toDouble()
                val vegetableScore = tokens[getScoreIndex(8)].toDouble()
                val fruitScore = tokens[getScoreIndex(19)].toDouble()
                val grainsCerealsScore = tokens[getScoreIndex(29)].toDouble()
                val wholeGrainsScore = tokens[getScoreIndex(33)].toDouble()
                val meatAlternativesScore = tokens[getScoreIndex(36)].toDouble()
                val dairyScore = tokens[getScoreIndex(40)].toDouble()
                val waterScore = tokens[getScoreIndex(49)].toDouble()
                val sodiumScore = tokens[getScoreIndex(43)].toDouble()
                val sugarScore = tokens[getScoreIndex(54)].toDouble()
                val alcoholScore = tokens[getScoreIndex(46)].toDouble()
                val saturatedFatScore = tokens[getScoreIndex(57)].toDouble()
                val unsaturatedFatScore = tokens[getScoreIndex(60)].toDouble()

                userList.add(
                    User(
                        phoneNumber, userId, sex, foodScore, discretionaryScore,
                        vegetableScore, fruitScore, grainsCerealsScore, wholeGrainsScore,
                        meatAlternativesScore, dairyScore, waterScore, sodiumScore,
                        sugarScore, alcoholScore, saturatedFatScore, unsaturatedFatScore
                    )
                )
            }

            reader.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return userList
    }

    fun getUserById(userId: String, context: Context): User? {
        val userList = loadUserData(context)
        return userList.find { it.userId == userId }
    }
}
