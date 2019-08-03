package com.domclick.config.security.encryption

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
class Encoders {
    @Bean
    fun bCryptFourStrengthEncoder() = BCryptPasswordEncoder(4)

    @Bean
    fun bCryptEightStrengthEncoder() = BCryptPasswordEncoder(8)
}