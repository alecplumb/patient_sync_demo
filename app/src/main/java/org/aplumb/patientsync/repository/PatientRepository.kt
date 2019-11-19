package org.aplumb.patientsync.repository

import io.reactivex.Completable
import io.reactivex.Observable
import org.aplumb.patientsync.sql.PatientPulse
import org.aplumb.patientsync.sql.PatientWithPulse

interface PatientRepository {
    fun getPatients(): Observable<List<PatientWithPulse>>
    fun getPatient(patientUuid: String): Observable<PatientWithPulse?>
    fun getPatientPulses(patientUuid: String): Observable<List<PatientPulse>>
    fun addPatient(name: String, heartRate:Double): Completable
    fun addPulse(patientUuid: String, heartRate: Double): Completable
}