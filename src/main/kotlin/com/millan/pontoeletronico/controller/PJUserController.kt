package com.millan.pontoeletronico.controller

import com.millan.pontoeletronico.domain.documents.Company
import com.millan.pontoeletronico.domain.documents.Employee
import com.millan.pontoeletronico.domain.enums.ProfileEnum
import com.millan.pontoeletronico.dto.CompanyFormDTO
import com.millan.pontoeletronico.response.Response
import com.millan.pontoeletronico.services.CompanyService
import com.millan.pontoeletronico.services.EmployeeService
import com.millan.pontoeletronico.utils.PasswordUtils
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.validation.ObjectError
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api/pj")
class PJUserController(val companyService: CompanyService, val employeeService: EmployeeService) {

    @PostMapping
    fun create(@Valid @RequestBody companyFormDTO: CompanyFormDTO, result: BindingResult)
            : ResponseEntity<Response<CompanyFormDTO>> {

        val response: Response<CompanyFormDTO> = Response()

        validateForm(companyFormDTO, result)

        if (result.hasErrors()) {
            for (error in result.allErrors) response.errors.add(error.defaultMessage!!)
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(response)
        }

        var company: Company = convertFormToCompany(companyFormDTO)
        company = companyService.save(company)

        var employee: Employee = covertFormToEmployee(companyFormDTO, company)
        employee = employeeService.save(employee)

        response.data = covertToCompanyFormDTO(employee, company)

        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    private fun covertToCompanyFormDTO(employee: Employee, company: Company): CompanyFormDTO =
        CompanyFormDTO(
            employee.name, employee.email, "", employee.cpf, company.cnpj,
            company.razaoSocial, employee.id
        )

    private fun covertFormToEmployee(form: CompanyFormDTO, company: Company): Employee =
        Employee(
            form.companyName, form.email, PasswordUtils().generateBcrypt(form.password),
            form.cpf, ProfileEnum.ROLE_ADMIN, company.id.toString()
        )

    private fun convertFormToCompany(companyFormDTO: CompanyFormDTO): Company =
        Company(companyFormDTO.companyName, companyFormDTO.cnpj)

    private fun validateForm(companyFormDTO: CompanyFormDTO, result: BindingResult) {

        val company: Company? = companyService.findByCnpj(companyFormDTO.cnpj)
        if (company != null) {
            result.addError(ObjectError("company", "Company CNPJ already exists"))
        }

        val employeeCpf: Employee? = employeeService.findByCpf(companyFormDTO.cpf)
        if (employeeCpf != null) {
            result.addError(ObjectError("employee", "Employee CPF already exists"))
        }

        val employeeEmail: Employee? = employeeService.findByEmail(companyFormDTO.email)
        if (employeeEmail != null) {
            result.addError(ObjectError("employee", "Employee email already exists"))
        }
    }
}