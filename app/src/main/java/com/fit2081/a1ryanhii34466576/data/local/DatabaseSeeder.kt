package com.fit2081.a1ryanhii34466576.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.fit2081.a1ryanhii34466576.data.repository.PatientRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object DatabaseSeeder {
    private const val PREFS_NAME = "database_prefs"
    private const val KEY_DB_SEEDED = "is_db_seeded"

    fun seedDatabaseIfNeeded(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        if (!isDatabaseSeeded(prefs)) {
            // Seed the database with initial data
            CoroutineScope(Dispatchers.IO).launch {
                val repository = PatientRepository(context)
                val patients = CsvReader.loadUserData(context)
                repository.insertAllPatients(patients)
                markDatabaseAsSeeded(prefs)
            }
        }
    }

    private fun isDatabaseSeeded(prefs: SharedPreferences): Boolean {
        return prefs.getBoolean(KEY_DB_SEEDED, false)
    }

    private fun markDatabaseAsSeeded(prefs: SharedPreferences) {
        prefs.edit { putBoolean(KEY_DB_SEEDED, true) }
    }
}
