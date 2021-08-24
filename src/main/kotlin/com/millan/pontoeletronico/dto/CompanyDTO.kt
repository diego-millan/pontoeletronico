package com.millan.pontoeletronico.dto

data class CompanyDTO (
    val name: String,
    val cnpj: String,
    val id:String? = null
)