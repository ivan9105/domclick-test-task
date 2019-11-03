package com.domclick.service.impl.oauth2

import com.domclick.repository.oauth2.OauthUserDetailsRepository
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@ConditionalOnExpression("'\${security.protocol}'!='jwt'")
@Service
class OauthUserDetailsServiceImpl(
        private val userDetailsRepository: OauthUserDetailsRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String) =
            userDetailsRepository.findByUsername(username) ?: throw UsernameNotFoundException(username)
}