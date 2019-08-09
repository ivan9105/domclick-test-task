package com.domclick.config.security.jwt.properties

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotBlank

@ConditionalOnProperty(name = ["security.protocol"], havingValue = "jwt")
@Configuration
@ConfigurationProperties(prefix = "jwt")
@Validated
class JwtSecurityProperties {
    var accessTokenThresholdInSec: Int = 3600
    @NotBlank
    lateinit var signingKey: String
    @NotBlank
    var tokenPrefix: String = "Bearer"
    @NotBlank
    var authorizationHeaderName: String = "Authorization"
}