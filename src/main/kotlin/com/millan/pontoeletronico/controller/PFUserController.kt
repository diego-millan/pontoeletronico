package com.millan.pontoeletronico.controller

import com.millan.pontoeletronico.domain.documents.Company
import com.millan.pontoeletronico.domain.documents.Employee
import com.millan.pontoeletronico.domain.enums.ProfileEnum
import com.millan.pontoeletronico.dto.EmployeeFormDTO
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
@RequestMapping("/api/pf")
class PFUserController(val companyService: CompanyService, val employeeService: EmployeeService) {

    @PostMapping
    fun create(@Valid @RequestBody employeeFormDTO: EmployeeFormDTO, result: BindingResult) :
            ResponseEntity<Response<EmployeeFormDTO>> {
        val response: Response<EmployeeFormDTO> = Response()

        val company: Company? = companyService?.findByCnpj(employeeFormDTO.cnpj)
        validateCompany(employeeFormDTO, company, result)

        if (result.hasErrors()) {
            for (error in result.allErrors) response.errors.add(error.defaultMessage!!)
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(response)
        }

        var employee: Employee = convertDTOToEmployee(employeeFormDTO, company!!)

        employee = employeeService.save(employee)
        response.data = convertEmployeeToDTO(employee, company!!)

        return ResponseEntity.status(HttpStatus.CREATED).body(response)

    }

    private fun convertEmployeeToDTO(employee: Employee, company: Company): EmployeeFormDTO? =
        EmployeeFormDTO(employee.name, employee.email, "", employee.cpf, company.cnpj,
        company.id.toString(), employee.hourValue.toString(), employee.dailyWorkedHours.toString(),
        employee.lunchTimeInHours.toString(), employee.id)

    private fun convertDTOToEmployee(form: EmployeeFormDTO, company: Company): Employee =
        Employee(form.name, form.email, PasswordUtils().generateBcrypt(form.password),
        form.cpf, ProfileEnum.ROLE_USER, company.id.toString(), form.hourValue?.toDouble(),
        form.workHoursDaily?.toFloat(), form.lunchTimeInHours?.toFloat(), form.id)


    private fun validateCompany(employeeFormDTO: EmployeeFormDTO,
                                company: Company?,
                                result: BindingResult) {

        if (company == null) {
            result.addError(ObjectError("company", "Company not found"))
        }

        val employeeCpf: Employee? = employeeService.findByCpf(employeeFormDTO.cpf)
        if (employeeCpf != null) {
            result.addError(ObjectError("employee", "Employee CPF already exists"))
        }

        val employeeEmail: Employee? = employeeService.findByEmail(employeeFormDTO.email)
        if (employeeEmail != null) {
            result.addError(ObjectError("employee", "Employee email already exists"))
        }
    }
}