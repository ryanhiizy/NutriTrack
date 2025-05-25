package com.fit2081.a1ryanhii34466576.data.api

import com.fit2081.a1ryanhii34466576.data.model.Fruit
import retrofit2.http.GET
import retrofit2.http.Path

interface FruityViceApi {
    @GET("api/fruit/{name}")
    suspend fun getFruitByName(@Path("name") name: String): Fruit
}
