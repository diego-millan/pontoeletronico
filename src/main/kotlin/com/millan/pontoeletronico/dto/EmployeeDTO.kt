package com.millan.pontoeletronico.dto

import org.hibernate.validator.constraints.Length
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty

data class EmployeeDTO (

        @get:NotEmpty(message = "Name must not be empty")
        @get:Length(min = 3, max = 200, message = "Name must be between 3 and 200 characters")
        val name: String = "",

        @get:NotEmpty(message = "E-mail must not bem empty")
        @get:Length(min = 5, max = 200, message = "E-mail must be between 5 and 200 characters")
        @get:Email(message = "invalid E-mail")
        val email: String = "",

        val password: String? = null,
        val hourValue: String? = null,
        val dailyWorkedHours: String? = null,
        val lunchTimeInHours: String? = null,
        val id: String? = null
)