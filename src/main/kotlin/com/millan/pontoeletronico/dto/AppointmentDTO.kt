package com.millan.pontoeletronico.dto

import javax.validation.constraints.NotEmpty

data class AppointmentDTO(

        @get:NotEmpty(message = "Date must not be empty")
        val date: String? = null,

        @get:NotEmpty(message = "Type must not be empty")
        val type: String? = null,

        val description: String? = null,
        val location: String? = null,
        val employeeId: String? = null,
        val id: String? = null,
)