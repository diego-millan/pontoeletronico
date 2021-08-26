package com.millan.pontoeletronico.service

import com.millan.pontoeletronico.domain.documents.Employee
import com.millan.pontoeletronico.domain.enums.ProfileEnum
import com.millan.pontoeletronico.repository.EmployeeRepository
import com.millan.pontoeletronico.services.EmployeeService
import com.millan.pontoeletronico.utils.PasswordUtils
import org.junit.Assert
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit4.SpringRunner
import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest
class EmployeeServiceTest {

    @MockBean
    private val employeeRepository: EmployeeRepository? = null

    @Autowired
    private val employeeService: EmployeeService? = null

    private val email: String = "mail@test.com"
    private val cpf: String = "07364412423"
    private val id: String = "1"

    @BeforeEach
    @Throws(Exception::class)
    fun setUp() {
        BDDMockito.given(employeeRepository?.save(Mockito.any(Employee::class.java)))
                .willReturn(createEmployee())
        BDDMockito.given(employeeRepository?.findById(id)).willReturn(Optional.of(createEmployee()))
        BDDMockito.given(employeeRepository?.findByEmail(email)).willReturn(createEmployee())
        BDDMockito.given(employeeRepository?.findByCpf(cpf)).willReturn(createEmployee())
    }

    @Test
    fun testSaveEmployee() {
        val employee: Employee? = this.employeeService?.save(createEmployee())
        Assert.assertNotNull(employee)
    }

    private fun createEmployee(): Employee = Employee(
        "Alexandra", email, PasswordUtils().generateBcrypt("password"), cpf,
        ProfileEnum.ROLE_USER, "1", 8.0, 8.0f, 1.0f, "1"
    )

    @Test
    fun testFindEmployeeById() {
        val employee: Optional<Employee>? = this.employeeService?.findById(id)
        Assert.assertNotNull(employee)
        if (employee != null) {
            Assert.assertFalse(employee.isEmpty)
        }
    }

    @Test
    fun testFindEmployeeByEmail() {
        val employee: Employee? = this.employeeService?.findByEmail(email)
        Assert.assertNotNull(employee)
    }

    @Test
    fun testFindEmployeeByCpf() {
        val employee: Employee? = this.employeeService?.findByCpf(cpf)
        Assert.assertNotNull(employee)
    }
}