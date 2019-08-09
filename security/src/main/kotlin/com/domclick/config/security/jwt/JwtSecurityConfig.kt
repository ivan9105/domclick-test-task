package com.domclick.config.security.jwt

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.security.SecurityProperties.BASIC_AUTH_ORDER
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@ConditionalOnProperty(name = ["security.protocol"], havingValue = "jwt")
@Configuration
@EnableWebSecurity(debug = true)
@Order(BASIC_AUTH_ORDER)
class JwtSecurityConfig(
        private val userDetailsService: UserDetailsService,
        private val unauthorizedPoint: JwtAuthenticationEntryPoint,
        private val bCryptFourStrengthEncoder: BCryptPasswordEncoder,
        private val authenticationFilter: JwtAuthenticationFilter
) : WebSecurityConfigurerAdapter() {

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptFourStrengthEncoder)
    }

    override fun configure(http: HttpSecurity) {
        http
                .authorizeRequests().antMatchers("/api/company/**").authenticated()
                .and()
                .authorizeRequests().antMatchers("/h2/**").permitAll()

        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter::class.java)

        http.csrf().disable()
        http.cors().disable()
        http.httpBasic().disable()
        http.headers().frameOptions().disable()
    }

    @Bean
    override fun authenticationManagerBean() = super.authenticationManagerBean()
}

//Todo test client jwt