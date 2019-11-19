package org.aplumb.patientsync.repository.api.model

data class ApiPatient(
    val id: Int,
    val uuid: String,
    val name: String,
    val dob: String
)