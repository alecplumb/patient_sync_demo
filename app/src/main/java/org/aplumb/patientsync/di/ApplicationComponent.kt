package org.aplumb.patientsync.di

import org.aplumb.patientsync.ui.*

interface ApplicationComponent {
    fun inject(fragment: PatientPulseDetailFragment)
    fun inject(activity: PatientPulseDetailActivity)
    fun inject(activity: PatientListActivity)
    fun inject(dialog: AddPatientDialog)
    fun inject(dialog: AddPulseDialog)
}