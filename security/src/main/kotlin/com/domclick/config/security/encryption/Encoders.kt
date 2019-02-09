package com.domclick.config.security.encryption

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
class Encoders {
    @Bean
    fun oauthClientPasswordEncoder() = BCryptPasswordEncoder(4)

    @Bean
    fun userPasswordEncoder() = BCryptPasswordEncoder(8)
}