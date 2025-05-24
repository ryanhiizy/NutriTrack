package com.fit2081.a1ryanhii34466576.data.repository

import android.content.Context
import com.fit2081.a1ryanhii34466576.data.local.NutriTrackDatabase
import com.fit2081.a1ryanhii34466576.data.model.NutriCoachTip
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class NutriCoachTipRepository(context: Context) {
    private val tipDao = NutriTrackDatabase.getDatabase(context).nutriCoachTipDao()

    fun getAllTips(): Flow<List<NutriCoachTip>> = tipDao.getAllTips()

    suspend fun insertTip(tip: NutriCoachTip) =
        withContext(Dispatchers.IO) { tipDao.insertTip(tip) }
}
