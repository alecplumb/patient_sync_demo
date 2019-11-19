package org.aplumb.patientsync.di.modules

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.aplumb.patientsync.BuildConfig
import org.aplumb.patientsync.repository.api.PatientApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import javax.inject.Named
import javax.inject.Singleton


@Module
class NetworkModule {
    @Provides
    @Singleton
    @Named("patient-api")
    fun providePatientApiGson(): Gson {
        val builder =
            GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        return builder.setLenient().create()
    }

    class OkHttpLog : HttpLoggingInterceptor.Logger {
        override fun log(message: String) {
            Timber.v(message)
        }
    }

    @Provides
    @Singleton
    @Named("patient-api")
    fun providePatientApiClient(): OkHttpClient {
        return OkHttpClient.Builder().apply {
            if (BuildConfig.DEBUG) {
                addInterceptor(
                    HttpLoggingInterceptor(OkHttpLog()).apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
            }
        }.build()
    }


    @Provides
    @Singleton
    @Named("patient-api")
    fun providePatientApiRetrofit(
        @Named("patient-api") gson: Gson,
        @Named("patient-api") okHttpClient: OkHttpClient
    ): Retrofit {

        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun providePatientApi(
        @Named("patient-api") retrofit: Retrofit
    ): PatientApi {
        return retrofit.create(PatientApi::class.java)
    }

}