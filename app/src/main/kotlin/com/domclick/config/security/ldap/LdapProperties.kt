package com.domclick.config.security.ldap

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import javax.validation.constraints.NotBlank

@ConditionalOnProperty(name = ["security.protocol"], havingValue = "ldap")
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