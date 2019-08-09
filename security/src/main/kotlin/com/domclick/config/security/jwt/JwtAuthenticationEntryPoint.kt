package com.domclick.config.security.jwt

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED

@ConditionalOnProperty(name = ["security.protocol"], havingValue = "jwt")
@Component
class JwtAuthenticationEntryPoint : AuthenticationEntryPoint{
    override fun commence(
            request: HttpServletRequest,
            response: HttpServletResponse,
            authException: AuthenticationException
    ) {
        response.sendError(SC_UNAUTHORIZED, "Unauthorized")
    }
}