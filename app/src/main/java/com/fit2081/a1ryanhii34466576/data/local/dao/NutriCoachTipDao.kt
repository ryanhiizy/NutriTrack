package com.fit2081.a1ryanhii34466576.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fit2081.a1ryanhii34466576.data.model.NutriCoachTip
import kotlinx.coroutines.flow.Flow

@Dao
interface NutriCoachTipDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTip(tip: NutriCoachTip)

    @Query("SELECT * FROM nutricoach_tips ORDER BY id DESC")
    fun getAllTips(): Flow<List<NutriCoachTip>>
}
