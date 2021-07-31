package com.millan.pontoeletronico.domain.documents

import com.millan.pontoeletronico.domain.enums.TypeEnum
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document
data class Appointment(
    val date: Date,
    val type: TypeEnum,
    val employeeId: String,
    val description: String? = "",
    val location: String? = "",
    @Id val id: String? = null
)