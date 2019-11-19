package org.aplumb.patientsync.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.aplumb.patientsync.ui.PatientListViewModel
import org.aplumb.patientsync.repository.PatientRepository
import org.aplumb.patientsync.ui.PatientDetailViewModel
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class ViewModelFactory @Inject constructor(
    val repository: PatientRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PatientListViewModel::class.java)) {
            return PatientListViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(PatientDetailViewModel::class.java)) {
            return PatientDetailViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown class")
    }
}