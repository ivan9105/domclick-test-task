package com.domclick.config.security.oauth2

import com.domclick.config.security.encryption.Encoders
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.security.SecurityProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder

@ConditionalOnProperty(name = ["security.protocol"], havingValue = "oauth2")
@Configuration
@EnableWebSecurity(debug = true)
@Order(SecurityProperties.BASIC_AUTH_ORDER)
@Import(Encoders::class)
class SecurityConfig(
        private val userPasswordEncoder: PasswordEncoder,
        private val userDetailsService: UserDetailsService
) : WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity) {
        http
                .authorizeRequests().antMatchers("/api/company/**").authenticated()
                .and()
                .authorizeRequests().antMatchers("/h2/**").permitAll()

        //use for enable h2 web client, todo use other vendor and disable it or add h2 profile and support some configuration files
        http.csrf().disable()
        http.httpBasic().disable()
        http.headers().frameOptions().disable()
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userDetailsService).passwordEncoder(userPasswordEncoder)
    }

    @Bean
    override fun authenticationManagerBean() = super.authenticationManagerBean()
}