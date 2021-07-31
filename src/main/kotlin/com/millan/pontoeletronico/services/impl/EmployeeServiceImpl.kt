package com.millan.pontoeletronico.services.impl

import com.millan.pontoeletronico.domain.documents.Employee
import com.millan.pontoeletronico.repository.EmployeeRepository
import com.millan.pontoeletronico.services.EmployeeService
import org.springframework.stereotype.Service
import java.util.*

@Service
class EmployeeServiceImpl(val repository: EmployeeRepository) : EmployeeService {
    override fun save(employee: Employee): Employee = repository.save(employee)

    override fun findByCpf(cpf: String): Employee? = repository.findByCpf(cpf)

    override fun findByEmail(email: String): Employee? = repository.findByEmail(email)

    override fun findById(id: String): Optional<Employee> = repository.findById(id)
}