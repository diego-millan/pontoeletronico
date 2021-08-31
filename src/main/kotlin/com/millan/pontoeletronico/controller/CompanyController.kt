package com.millan.pontoeletronico.controller

import com.millan.pontoeletronico.domain.documents.Company
import com.millan.pontoeletronico.dto.CompanyDTO
import com.millan.pontoeletronico.response.Response
import com.millan.pontoeletronico.services.CompanyService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/api/company/")
class CompanyController(val service: CompanyService) {

    @GetMapping(value = ["/cnpj/{cnpj}"])
    fun findByCnpj(@PathVariable("cnpj") cnpj : String) : ResponseEntity<Response<CompanyDTO>> {
        val response : Response<CompanyDTO> = Response()
        val company : Optional<Company> = service.findByCnpj(cnpj)
        if (company.isEmpty) {
            response.errors.add("Company not found for CNPJ $cnpj")
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response)
        }

        response.data = convertCompanyToDTO(company.get())
        return ResponseEntity.ok(response)
    }

    private fun convertCompanyToDTO(company: Company): CompanyDTO {
        return CompanyDTO(company.razaoSocial, company.cnpj, company.id)
    }
}