package org.aplumb.patientsync.repository.api.model

data class ApiPatientPulse(
    val uuid: String,
    val patientUuid: String,
    val heartRate: Double,
    val timestamp: String
)