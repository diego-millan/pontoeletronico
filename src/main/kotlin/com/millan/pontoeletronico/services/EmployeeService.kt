package com.millan.pontoeletronico.services

import com.millan.pontoeletronico.domain.documents.Employee
import java.util.*

interface EmployeeService {
    fun save(employee:Employee): Employee
    fun findByCpf(cpf:String): Employee?
    fun findByEmail(email:String): Employee?
    fun findById(id:String): Optional<Employee>
}