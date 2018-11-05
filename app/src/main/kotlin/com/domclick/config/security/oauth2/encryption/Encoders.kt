package com.domclick.config.security.oauth2.encryption

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@ConditionalOnProperty(name = ["security.protocol"], havingValue = "oauth2")
@Configuration
class Encoders {
    @Bean
    fun oauthClientPasswordEncoder() = BCryptPasswordEncoder(4)

    @Bean
    fun userPasswordEncoder() = BCryptPasswordEncoder(8)
}