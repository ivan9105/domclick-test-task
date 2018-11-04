package com.domclick.service.oauth2

import com.domclick.repository.oauth2.UserDetailsRepository
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl(private val userDetailsRepository: UserDetailsRepository) : UserDetailsService {
    override fun loadUserByUsername(username: String) = userDetailsRepository.findByUsername(username)
            ?: throw UsernameNotFoundException(username)
}