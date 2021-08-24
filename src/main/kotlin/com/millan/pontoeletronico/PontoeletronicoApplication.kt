package com.millan.pontoeletronico

import com.millan.pontoeletronico.domain.documents.Company
import com.millan.pontoeletronico.domain.documents.Employee
import com.millan.pontoeletronico.domain.enums.ProfileEnum
import com.millan.pontoeletronico.repository.CompanyRepository
import com.millan.pontoeletronico.repository.EmployeeRepository
import com.millan.pontoeletronico.utils.PasswordUtils
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PontoeletronicoApplication(val companyRepository: CompanyRepository,
                                 val employeeRepository: EmployeeRepository) {
//    : CommandLineRunner {

//    override fun run(args: Array<String>) {
//        companyRepository.deleteAll()
//        employeeRepository.deleteAll()
//
//        val company: Company = Company("Company1", "05.136.902/0001-73")
//        companyRepository.save(company)
//
//        val admin: Employee = Employee("Admin", "adm@compa.ny",
//                PasswordUtils().generateBcrypt("123456"), "07375512367",
//                ProfileEnum.ROLE_ADMIN, company.id!!)
//        employeeRepository.save(admin)
//
//        val employee: Employee = Employee("Employee", "employee@compa.ny",
//                PasswordUtils().generateBcrypt("123456"), "07375512368",
//                ProfileEnum.ROLE_USER, company.id!!)
//		employeeRepository.save(employee)
//
//		println("Company Id: " + company.id)
//		println("Admin Id: " + admin.id)
//		println("User Id: " + employee.id)
//    }

	fun main (args: Array<String>) {
		runApplication<PontoeletronicoApplication>(*args)
	}
}
