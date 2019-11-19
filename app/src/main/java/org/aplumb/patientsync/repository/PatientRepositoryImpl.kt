package org.aplumb.patientsync.repository

import com.squareup.sqldelight.runtime.rx.asObservable
import com.squareup.sqldelight.runtime.rx.mapToList
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import org.aplumb.patientsync.Database
import org.aplumb.patientsync.repository.api.PatientApi
import org.aplumb.patientsync.repository.api.model.ApiPatient
import org.aplumb.patientsync.repository.api.model.ApiPatientPulse
import org.aplumb.patientsync.sql.PatientPulse
import org.aplumb.patientsync.sql.PatientWithPulse
import org.aplumb.patientsync.utils.isoDateTimeFormat
import java.util.*

class PatientRepositoryImpl(
    val patientApi: PatientApi,
    val patientDb: Database
) : PatientRepository {
    private fun updatePatient(patient: ApiPatient) {
        patientDb.patientQueries
            .insertOrReplace(
                patient.uuid,
                patient.id.toLong(),
                patient.name
            )
    }

    private fun updatePulse(pulse: ApiPatientPulse) {
        patientDb.patientPulseQueries
            .insertOrReplace(
                pulse.uuid,
                pulse.patientUuid,
                pulse.timestamp,
                pulse.heartRate
            )
    }

    private fun updatePatients(patients: List<ApiPatient>) {
        patients.forEach(::updatePatient)
    }

    private fun updatePulses(pulses: List<ApiPatientPulse>) {
        pulses.forEach(::updatePulse)
    }

    private fun refreshPatientsFromApi(): Completable {
        return Observable.combineLatest(
            patientApi.getPatients(),
            patientApi.getPulses(),
            BiFunction<List<ApiPatient>, List<ApiPatientPulse>, Boolean>
            { patients, pulses ->
                updatePatients(patients)
                updatePulses(pulses)
                true
            }
        ).ignoreElements()
    }

    private fun refreshPatientFromApi(patientUuid: String): Completable {
        return patientApi.getPatient(patientUuid)
            .doOnNext(::updatePatients)
            .ignoreElements()
    }

    private fun refreshPatientPulsesFromApi(patientUuid: String): Completable {
        return patientApi.getPatientPulses(patientUuid)
            .doOnNext(::updatePulses)
            .ignoreElements()
    }

    override fun getPatients(): Observable<List<PatientWithPulse>> {
        return Observable.mergeDelayError(
            refreshPatientsFromApi().toObservable(),
            patientDb.patientQueries.selectAll()
                .asObservable()
                .mapToList()
        )
            .subscribeOn(Schedulers.io())
    }

    override fun getPatient(patientUuid: String): Observable<PatientWithPulse?> {
        return Observable.mergeDelayError(
            refreshPatientFromApi(patientUuid).toObservable(),
            patientDb.patientQueries.selectByUuid(patientUuid)
                .asObservable()
                .mapToList()
                .map { it.firstOrNull() }
        )
            .subscribeOn(Schedulers.io())
    }

    override fun getPatientPulses(patientUuid: String): Observable<List<PatientPulse>> {
        return Observable.mergeDelayError(
            refreshPatientPulsesFromApi(patientUuid).toObservable(),
            patientDb.patientPulseQueries.selectByPatientUuid(patientUuid)
                .asObservable()
                .mapToList()
        )
            .subscribeOn(Schedulers.io())
    }

    override fun addPatient(name: String, heartRate: Double): Completable {
        // TODO: writable backend
        return Completable.fromCallable {
            val patientUuid = UUID.randomUUID().toString()
            val pulseUuid = UUID.randomUUID().toString()
            patientDb.patientQueries.insertOrReplace(patientUuid, null, name)
            patientDb.patientPulseQueries.insertOrReplace(pulseUuid, patientUuid, isoDateTimeFormat.format(Date()), heartRate)
        }.subscribeOn(Schedulers.io())
    }

    override fun addPulse(patientUuid: String, heartRate: Double): Completable {
        // TODO: writable backend
        return Completable.fromCallable {
            val pulseUuid = UUID.randomUUID().toString()
            patientDb.patientPulseQueries.insertOrReplace(pulseUuid, patientUuid, isoDateTimeFormat.format(Date()), heartRate)
        }.subscribeOn(Schedulers.io())
    }

}