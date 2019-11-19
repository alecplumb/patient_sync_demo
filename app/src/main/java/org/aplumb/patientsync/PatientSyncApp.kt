package org.aplumb.patientsync

import android.app.Application
import org.aplumb.patientsync.di.ApplicationComponent
import org.aplumb.patientsync.di.DaggerMainApplicationComponent
import org.aplumb.patientsync.di.modules.DatabaseModule
import timber.log.Timber
import timber.log.Timber.DebugTree


class PatientSyncApp : Application() {
    companion object {
        lateinit var app: PatientSyncApp

        fun component(): ApplicationComponent {
            return app._component
        }
    }

    lateinit var _component: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        app = this

        _component = DaggerMainApplicationComponent
            .builder()
            .databaseModule(DatabaseModule(applicationContext))
            .build()

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }
}