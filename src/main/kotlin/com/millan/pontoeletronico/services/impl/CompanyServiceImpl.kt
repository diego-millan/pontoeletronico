package com.millan.pontoeletronico.services.impl

import com.millan.pontoeletronico.domain.documents.Company
import com.millan.pontoeletronico.repository.CompanyRepository
import com.millan.pontoeletronico.services.CompanyService
import org.springframework.stereotype.Service
import java.util.*

@Service
class CompanyServiceImpl(val companyRepository: CompanyRepository) : CompanyService {

    override fun findByCnpj(cnpj: String): Optional<Company> =
        Optional.ofNullable(companyRepository.findByCnpj(cnpj))

    override fun save(company: Company): Company = companyRepository.save(company)

}