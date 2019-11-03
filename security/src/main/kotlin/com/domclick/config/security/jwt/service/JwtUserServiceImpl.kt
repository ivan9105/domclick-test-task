package com.domclick.config.security.jwt.service

import com.domclick.entity.jwt.JwtUserDetailsEntity
import com.domclick.repository.jwt.JwtUserDetailsRepository
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.Arrays.asList

@ConditionalOnProperty(name = ["security.protocol"], havingValue = "jwt")
@Service
class JwtUserServiceImpl(
        private val repository: JwtUserDetailsRepository,
        private val bCryptFourStrengthEncoder: BCryptPasswordEncoder
) : UserDetailsService, JwtUserService {

    override fun save(username: String, password: String) = repository.save(
            JwtUserDetailsEntity().apply {
                this.username = username
                this.password = bCryptFourStrengthEncoder.encode(password)
            }
    )

    override fun loadUserByUsername(username: String): UserDetails {
        val userOptional = repository.findByUsername(username)
        if (!userOptional.isPresent) {
            throw UsernameNotFoundException("Invalid username or password.")
        }
        val jwtUser = userOptional.get()
        return User(jwtUser.username, jwtUser.password, mockAuthority())
    }

    private fun mockAuthority() = asList(SimpleGrantedAuthority("ROLE_ADMIN"))

}