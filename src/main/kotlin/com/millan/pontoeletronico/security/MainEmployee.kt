package com.millan.pontoeletronico.security

import com.millan.pontoeletronico.domain.documents.Employee
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class MainEmployee(val employee: Employee) : UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {

        val authorities : MutableCollection<GrantedAuthority> = mutableListOf()
        authorities.add(SimpleGrantedAuthority(employee.profile.toString()))

        return authorities
    }

    override fun getPassword(): String = employee.password

    override fun getUsername(): String = employee.email

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true

}