package com.millan.pontoeletronico.services

import com.millan.pontoeletronico.domain.documents.Company
import java.util.*

interface CompanyService {
    fun findByCnpj(cnpj:String) : Optional<Company>
    fun save(company:Company): Company
}