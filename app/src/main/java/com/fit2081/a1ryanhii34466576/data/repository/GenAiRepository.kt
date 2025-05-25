package com.fit2081.a1ryanhii34466576.data.repository

import android.util.Log
import com.fit2081.a1ryanhii34466576.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content

class GenAiRepository {
    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = BuildConfig.GEMINI_API_KEY
    )

    suspend fun prompt(text: String): String? {
        return try {
            val response = generativeModel.generateContent(
                content {
                    text(text)
                }
            )
            response.text
        } catch (e: Exception) {
            Log.e("GenAiRepository", "AI generation failed: ${e.message}", e)
            null
        }
    }
}
