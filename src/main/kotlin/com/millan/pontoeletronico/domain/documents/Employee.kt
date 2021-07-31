package com.millan.pontoeletronico.domain.documents

import com.millan.pontoeletronico.domain.enums.ProfileEnum
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Employee (
    val name: String,
    val email: String,
    val password: String,
    val cpf: String,
    val profile: ProfileEnum,
    val companyId: String,
    val hourValue: Double,
    val dailyWorkedHours: Float? = 0.0f,
    val lunchTimeInHours: Float? = 0.0f,
    @Id val id: String? = null
)