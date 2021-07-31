package com.millan.pontoeletronico.services

import com.millan.pontoeletronico.domain.documents.Company

interface CompanyService {
    fun findByCnpj(cnpj:String) : Company?
    fun save(company:Company): Company
}