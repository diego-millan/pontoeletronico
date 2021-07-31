package com.millan.pontoeletronico.utils

import org.junit.Assert
import org.junit.jupiter.api.Test
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

class PasswordUtilsTest {
    private val password = "password"
    private val bCryptPasswordEncoder = BCryptPasswordEncoder()

    @Test
    fun testGenerateHashPassword() {
        val hash = PasswordUtils().generateBcrypt(password)
        Assert.assertTrue(bCryptPasswordEncoder.matches(password, hash))
    }
}