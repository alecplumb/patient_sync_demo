package org.aplumb.patientsync.repository.api

import io.reactivex.Observable
import org.aplumb.patientsync.repository.api.model.ApiPatient
import org.aplumb.patientsync.repository.api.model.ApiPatientPulse
import retrofit2.http.GET
import retrofit2.http.Query

interface PatientApi {
    @GET("patients")
    fun getPatients(): Observable<List<ApiPatient>>

    @GET("patients")
    fun getPatient(@Query("uuid") patientUuid: String): Observable<List<ApiPatient>>

    @GET("pulses")
    fun getPulses(): Observable<List<ApiPatientPulse>>

    @GET("pulses")
    fun getPatientPulses(@Query("patient_uuid") patientUuid: String): Observable<List<ApiPatientPulse>>

}