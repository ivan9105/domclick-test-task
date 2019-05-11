package com.domclick.config.properties.oauth2

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotBlank

@Configuration
@ConfigurationProperties(prefix = "oauth2")
@Validated
class Oauth2Properties {
    @NotBlank
    lateinit var host: String
    @NotBlank
    lateinit var port: String
}