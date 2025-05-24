package com.fit2081.a1ryanhii34466576.data.repository

import android.content.Context
import com.fit2081.a1ryanhii34466576.data.local.NutriTrackDatabase
import com.fit2081.a1ryanhii34466576.data.model.Patient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class PatientRepository(context: Context) {
    private val patientDao = NutriTrackDatabase.getDatabase(context).patientDao()

    fun getAllPatients(): Flow<List<Patient>> = patientDao.getAllPatients()

    suspend fun getPatientById(userId: String): Patient? =
        withContext(Dispatchers.IO) { patientDao.getPatientById(userId) }

    suspend fun insertPatient(patient: Patient) =
        withContext(Dispatchers.IO) { patientDao.insertPatient(patient) }

    suspend fun insertAllPatients(patients: List<Patient>) =
        withContext(Dispatchers.IO) { patientDao.insertAllPatients(patients) }
}
    