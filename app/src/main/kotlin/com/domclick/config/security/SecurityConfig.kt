package com.domclick.config.security

import com.domclick.config.security.encryption.Encoders
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

@Configuration
@EnableWebSecurity
@Order(SecurityProperties.BASIC_AUTH_ORDER)
@Import(Encoders::class)
class SecurityConfig(private val userPasswordEncoder: PasswordEncoder,
                     private val userDetailsService: UserDetailsService) : WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity) {
        //Todo add oauth/** pattern instead of /api/account/list
        http
                .authorizeRequests().antMatchers("/api/account/list").authenticated()
                .and()
                .authorizeRequests().antMatchers("/h2/**").permitAll()

        //use for enable h2 web client, todo use other vendor and disable it
        http.csrf().disable()
        http.headers().frameOptions().disable()
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userDetailsService).passwordEncoder(userPasswordEncoder)
    }

    @Bean
    override fun authenticationManagerBean() = super.authenticationManagerBean()
}