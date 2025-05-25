package com.fit2081.a1ryanhii34466576.data.repository

import com.fit2081.a1ryanhii34466576.data.api.RetrofitInstance
import com.fit2081.a1ryanhii34466576.data.model.Fruit

class FruitRepository {
    suspend fun getFruitByName(name: String): Fruit {
        return RetrofitInstance.fruityViceApi.getFruitByName(name)
    }
}
