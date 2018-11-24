package com.domclick.config.properties.ldap

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import javax.validation.constraints.NotBlank

@Configuration
@ConfigurationProperties(prefix = "spring.ldap.embedded")
class LdapEmbeddedProperties {
    @NotBlank
    lateinit var baseDn: String
}