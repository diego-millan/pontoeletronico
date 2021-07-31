package com.millan.pontoeletronico.service

import com.millan.pontoeletronico.domain.documents.Company
import com.millan.pontoeletronico.repository.CompanyRepository
import com.millan.pontoeletronico.services.CompanyService
import org.junit.Assert
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
class CompanyServiceTest {

    @Autowired
    val companyService: CompanyService? = null

    @MockBean
    private val companyRepository:CompanyRepository? = null

    private val cnpj = "1234567890"

    @BeforeEach
    @Throws(Exception::class)
    fun setUp() {
        BDDMockito.given(companyRepository?.findByCnpj(cnpj)).willReturn(company())
        BDDMockito.given(companyRepository?.save(company())).willReturn(company())
    }

    @Test
    fun testFindCompanyByCnpj() {
        val company: Company? = companyService?.findByCnpj(cnpj)
        Assert.assertNotNull(company)
    }

    @Test
    fun testSaveCompany() {
        val company: Company? = companyService?.save(company())
        Assert.assertNotNull(company)
    }

    private fun company(): Company = Company("Company Test", cnpj, "1")

}