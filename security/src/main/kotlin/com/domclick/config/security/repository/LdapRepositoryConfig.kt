package com.domclick.config.security.repository

import com.domclick.config.security.config.properties.ldap.LdapEmbeddedProperties
import com.domclick.config.security.config.properties.ldap.LdapProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.ldap.core.ContextSource
import org.springframework.ldap.core.LdapTemplate
import org.springframework.ldap.core.support.LdapContextSource

@Configuration
//@EnableLdapRepositories(basePackages = ["com.domclick.repository.ldap.**"])
class LdapRepositoryConfig(
        private val ldapProperties: LdapProperties,
        private val ldapEmbeddedProperties: LdapEmbeddedProperties
) {

    @Bean
    fun ldapContextSource(): ContextSource {
        return LdapContextSource().apply {
            urls = arrayOf(ldapProperties.url)
            userDn = ldapProperties.userDnPatterns
            setBase(ldapEmbeddedProperties.baseDn)
        }
    }

    @Bean
    fun ldapTemplate() = LdapTemplate(ldapContextSource())
}