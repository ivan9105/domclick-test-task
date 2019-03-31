package com.domclick.config.security.ldap

import com.domclick.config.security.ldap.client.LdapClient
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.ldap.repository.config.EnableLdapRepositories
import org.springframework.ldap.core.LdapTemplate
import org.springframework.ldap.core.support.LdapContextSource

@ConditionalOnProperty(name = ["ldap.persistence.enable"], havingValue = "true")
@Configuration
@EnableLdapRepositories(basePackages = ["com.domclick.config.security.ldap.repository.**"])
class LdapConfig(
        private val properties: LdapSecurityProperties
) {

    @Bean
    fun contextSource() =
            LdapContextSource().apply {
                setUrl(properties.url)
                setBase(properties.partitionSuffix)
                userDn = properties.principal
                password = properties.password
            }

    @Bean
    fun ldapTemplate() = LdapTemplate(contextSource())

    @Bean
    fun ldapClient() = LdapClient(contextSource(), ldapTemplate(), properties)
}