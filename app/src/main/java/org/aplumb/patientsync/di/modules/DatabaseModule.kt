package org.aplumb.patientsync.di.modules

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import dagger.Module
import dagger.Provides
import org.aplumb.patientsync.Database
import javax.inject.Named
import javax.inject.Singleton

@Module
class DatabaseModule(val applicationContext: Context) {
    @Singleton
    @Provides
    @Named("patient-db")
    fun provideAndroidSqliteDriver(): AndroidSqliteDriver {
        return AndroidSqliteDriver(
            schema = Database.Schema,
            context = applicationContext,
            name = "patients.db"
        )
    }

    @Singleton
    @Provides
    @Named("patient-db")
    fun providePatientDatabase(
        @Named("patient-db") driver: AndroidSqliteDriver
    ): Database {
        return Database(driver)
    }
}
