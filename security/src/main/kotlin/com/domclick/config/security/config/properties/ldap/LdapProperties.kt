package com.domclick.config.security.config.properties.ldap

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import javax.validation.constraints.NotBlank

@Configuration
@ConfigurationProperties(prefix = "security.ldap")
class LdapProperties {
    @NotBlank
    lateinit var userDnPatterns: String
    @NotBlank
    lateinit var groupSearchBase: String
    @NotBlank
    lateinit var url: String
    @NotBlank
    lateinit var passwordAttribute: String
}