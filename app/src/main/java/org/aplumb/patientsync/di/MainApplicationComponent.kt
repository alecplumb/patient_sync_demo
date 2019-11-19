package org.aplumb.patientsync.di

import dagger.Component
import org.aplumb.patientsync.di.modules.DatabaseModule
import org.aplumb.patientsync.di.modules.NetworkModule
import org.aplumb.patientsync.di.modules.RepositoryModule
import org.aplumb.patientsync.di.modules.ViewModelModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        DatabaseModule::class,
        NetworkModule::class,
        RepositoryModule::class,
        ViewModelModule::class]
)
interface MainApplicationComponent : ApplicationComponent {
}