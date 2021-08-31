package com.millan.pontoeletronico.controller

import com.millan.pontoeletronico.domain.documents.Employee
import com.millan.pontoeletronico.dto.EmployeeDTO
import com.millan.pontoeletronico.response.Response
import com.millan.pontoeletronico.services.EmployeeService
import com.millan.pontoeletronico.utils.PasswordUtils
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/employee/")
class EmployeeController(val service: EmployeeService) {

    @PutMapping(value = ["id"])
    fun update(
        @PathVariable("id") id: String,
        @RequestBody employeeDTO: EmployeeDTO,
        result: BindingResult ): ResponseEntity<Response<EmployeeDTO>> {

        val response : Response<EmployeeDTO> = Response()
        val employee : Optional<Employee> = service.findById(id)

        if (employee.isEmpty) {
            response.errors.add("Employee not found by id $id")
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response)
        }

        val employeeUpdate : Employee = updateEmployeeData(employee.get(), employeeDTO)
        service.save(employeeUpdate)
        response.data = convertEmployeeToDTO(employeeUpdate)
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response)
    }

    private fun convertEmployeeToDTO(employee: Employee): EmployeeDTO {
        return EmployeeDTO(employee.name, employee.email, "", employee.hourValue.toString(),
        employee.dailyWorkedHours.toString(), employee.lunchTimeInHours.toString(), employee.id)
    }

    private fun updateEmployeeData(employee: Employee, employeeDTO: EmployeeDTO): Employee {
        var password: String
        if (employeeDTO.password == null) {
            password = employee.password
        } else {
            password = PasswordUtils().generateBcrypt(employeeDTO.password)
        }

        return Employee(employeeDTO.name, employeeDTO.email, password, employee.cpf,
            employee.profile, employee.companyId, employeeDTO.hourValue?.toDouble(),
            employeeDTO.dailyWorkedHours?.toFloat(), employeeDTO.lunchTimeInHours?.toFloat(),
            employee.id)
    }
}