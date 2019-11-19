package org.aplumb.patientsync.di.modules

import dagger.Module
import dagger.Provides
import org.aplumb.patientsync.Database
import org.aplumb.patientsync.repository.PatientRepository
import org.aplumb.patientsync.repository.PatientRepositoryImpl
import org.aplumb.patientsync.repository.api.PatientApi
import javax.inject.Named
import javax.inject.Singleton

@Module
class RepositoryModule {
    @Provides
    @Singleton
    fun providePatientSyncRepository(
        api: PatientApi,
        @Named("patient-db") db: Database
    ): PatientRepository {
        return PatientRepositoryImpl(api, db)
    }
}