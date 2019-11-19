package org.aplumb.patientsync.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import org.aplumb.patientsync.repository.PatientRepository
import org.aplumb.patientsync.sql.PatientPulse
import org.aplumb.patientsync.sql.PatientWithPulse
import timber.log.Timber

class PatientDetailViewModel(val repository: PatientRepository) : ViewModel() {

    val patient = MutableLiveData<PatientWithPulse>()
    val pulses = MutableLiveData<List<PatientPulse>>(emptyList())

    private val disposables: CompositeDisposable = CompositeDisposable()

    fun updatePatient(patientUuid: String) {
        disposables.clear()
        disposables.addAll(
            repository.getPatient(patientUuid)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    patient.value = result
                }, { error ->
                    // TODO: user error notification
                    Timber.v(error, "Failed to lookup patient")
                }),
            repository.getPatientPulses(patientUuid)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    pulses.value = result
                }, { error ->
                    // TODO: user error notification
                    Timber.v(error, "Failed to lookup patient")
                })
            )

    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

}