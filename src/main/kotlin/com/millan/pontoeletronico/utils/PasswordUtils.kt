package com.millan.pontoeletronico.utils

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

class PasswordUtils {
    fun generateBcrypt(password:String): String = BCryptPasswordEncoder().encode(password)
}