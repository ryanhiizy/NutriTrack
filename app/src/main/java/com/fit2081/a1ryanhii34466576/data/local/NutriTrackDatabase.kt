package com.fit2081.a1ryanhii34466576.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fit2081.a1ryanhii34466576.data.local.dao.FoodIntakeDao
import com.fit2081.a1ryanhii34466576.data.local.dao.NutriCoachTipDao
import com.fit2081.a1ryanhii34466576.data.local.dao.PatientDao
import com.fit2081.a1ryanhii34466576.data.model.FoodIntake
import com.fit2081.a1ryanhii34466576.data.model.NutriCoachTip
import com.fit2081.a1ryanhii34466576.data.model.Patient

@Database(
    entities = [Patient::class, FoodIntake::class, NutriCoachTip::class],
    version = 5,
    exportSchema = false
)
abstract class NutriTrackDatabase : RoomDatabase() {
    abstract fun patientDao(): PatientDao
    abstract fun foodIntakeDao(): FoodIntakeDao
    abstract fun nutriCoachTipDao(): NutriCoachTipDao

    companion object {
        @Volatile
        private var Instance: NutriTrackDatabase? = null

        fun getDatabase(context: Context): NutriTrackDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    NutriTrackDatabase::class.java,
                    "nutritrack_database"
                )
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
