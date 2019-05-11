package com.domclick.config.security.ldap

import com.domclick.config.security.encryption.Encoders
import com.domclick.config.security.ldap.client.LdapClient.Companion.USER_PASSWORD_ENCODED_NAME_ATTR
import com.domclick.config.security.ldap.encoder.LdapShaPasswordEncoder
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.security.SecurityProperties.BASIC_AUTH_ORDER
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@ConditionalOnProperty(name = ["security.protocol"], havingValue = "ldap")
@Configuration
@EnableWebSecurity(debug = true)
@Order(BASIC_AUTH_ORDER)
@Import(Encoders::class)
class LdapSecurityConfig(
        private val properties: LdapSecurityProperties
) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http
                .authorizeRequests()
                .anyRequest().fullyAuthenticated()
                .and().httpBasic()
    }

    public override fun configure(auth: AuthenticationManagerBuilder) {
        auth
                .ldapAuthentication()
                .userDnPatterns(properties.userDn)
                .contextSource()
                .url("${properties.url}/${properties.partitionSuffix}")
                .managerDn(properties.principal)
                .managerPassword(properties.password)
                .and()
                .passwordCompare()
                .passwordEncoder(LdapShaPasswordEncoder())
                .passwordAttribute(USER_PASSWORD_ENCODED_NAME_ATTR)
    }
}