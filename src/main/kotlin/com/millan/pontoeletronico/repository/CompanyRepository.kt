package com.millan.pontoeletronico.repository

import com.millan.pontoeletronico.domain.documents.Company
import org.springframework.data.mongodb.repository.MongoRepository

interface CompanyRepository : MongoRepository<Company, String> {
    fun findByCnpj(cnpj:String): Company?
}