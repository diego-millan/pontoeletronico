package com.millan.pontoeletronico.security

import com.millan.pontoeletronico.domain.documents.Employee
import com.millan.pontoeletronico.services.EmployeeService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class EmployeeDetailsService (val service : EmployeeService) : UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails {

        if (username != null) {

            val employee : Employee? = service.findByEmail(username)

            if (employee != null) {
                return MainEmployee(employee)
            }
        }

        throw UsernameNotFoundException(username)
    }
}