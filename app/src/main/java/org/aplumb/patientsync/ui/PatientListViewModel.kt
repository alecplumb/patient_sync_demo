package org.aplumb.patientsync.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import org.aplumb.patientsync.repository.PatientRepository
import org.aplumb.patientsync.sql.PatientWithPulse
import timber.log.Timber

class PatientListViewModel(val repository: PatientRepository)  : ViewModel(){

    val patients = MutableLiveData<List<PatientWithPulse>>(emptyList())

    private var disposable: Disposable? = null

    fun updatePatients() {
        disposable?.dispose()
        disposable =
            repository.getPatients()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    patients.value = result
                }, { error ->
                    // TODO: user error notification
                    Timber.v(error, "Failed to lookup patients")
                })
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose()
    }

}