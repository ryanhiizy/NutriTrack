package com.fit2081.a1ryanhii34466576.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val fruityViceApi: FruityViceApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.fruityvice.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FruityViceApi::class.java)
    }
}
