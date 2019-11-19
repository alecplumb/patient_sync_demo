package org.aplumb.patientsync.di.modules

import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import org.aplumb.patientsync.di.ViewModelFactory
import org.aplumb.patientsync.repository.PatientRepository
import javax.inject.Singleton


@Module
class ViewModelModule {
    @Provides
    @Singleton
    fun getViewModelFactory(repository: PatientRepository): ViewModelProvider.Factory {
        return ViewModelFactory(repository)
    }

}