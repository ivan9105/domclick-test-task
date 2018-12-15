package com.domclick.config.security.ldap

import com.domclick.config.properties.ldap.LdapProperties
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.security.SecurityProperties
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.password.PasswordEncoder

@ConditionalOnProperty(name = ["security.protocol"], havingValue = "ldap")
@Configuration
@EnableWebSecurity
@Order(SecurityProperties.BASIC_AUTH_ORDER)
class SecurityConfig(
        private val userPasswordEncoder: PasswordEncoder,
        private val ldapProperties: LdapProperties
) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http.authorizeRequests()
                .antMatchers("/api/**").authenticated()
                .and()
                .httpBasic()
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        //Todo ldap controller
        //Todo spring ldap dto converters
        //Todo ldap template
        //Todo repository test
        auth
                .ldapAuthentication()
                .userDnPatterns(ldapProperties.userDnPatterns)
                .groupSearchBase(ldapProperties.groupSearchBase)
                .contextSource()
                .url(ldapProperties.url)
                .and()
                .passwordCompare()
                .passwordEncoder(userPasswordEncoder)
                .passwordAttribute(ldapProperties.passwordAttribute)
    }
}
