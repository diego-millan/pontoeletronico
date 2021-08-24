package com.millan.pontoeletronico.dto

import org.hibernate.validator.constraints.Length
import org.hibernate.validator.constraints.br.CNPJ
import org.hibernate.validator.constraints.br.CPF
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty

data class EmployeeFormDTO (
        @get:NotEmpty(message = "Name must not be empty")
        @get:Length(min = 3, max = 200, message = "Name must be between 3 and 200 characters")
        val name: String = "",

        @get:NotEmpty(message = "E-mail must not bem empty")
        @get:Length(min = 5, max = 200, message = "E-mail must be between 5 and 200 characters")
        @get:Email(message = "invalid E-mail")
        val email: String = "",

        @get:NotEmpty(message = "Password must not bem empty")
        val password: String = "",

        @get:NotEmpty(message = "CPF must not bem empty")
        @get:CPF(message = "CPF is not valid")
        val cpf: String = "",

        @get:NotEmpty(message = "CNPJ must not bem empty")
        @get:CNPJ(message = "CNPJ is not valid")
        val cnpj: String = "",

        val companyId: String = "",
        val hourValue: String? = null,
        val workHoursDaily: String? = null,
        val lunchTimeInHours: String? = null,

        val id: String? = null
)
