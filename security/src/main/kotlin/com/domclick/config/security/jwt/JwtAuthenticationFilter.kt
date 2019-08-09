package com.domclick.config.security.jwt

import com.domclick.config.security.jwt.properties.JwtSecurityProperties
import com.domclick.config.security.jwt.util.JwtTokenUtil
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.SignatureException
import mu.KLogging
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@ConditionalOnProperty(name = ["security.protocol"], havingValue = "jwt")
@Component
class JwtAuthenticationFilter(
        private val utils: JwtTokenUtil,
        private val properties: JwtSecurityProperties,
        private val userDetailsService: UserDetailsService
) : OncePerRequestFilter() {

    companion object : KLogging()

    override fun doFilterInternal(
            request: HttpServletRequest,
            response: HttpServletResponse,
            filterChain: FilterChain
    ) {
        val authorizationHeader: String? = request.getHeader(properties.authorizationHeaderName)
        val isValidHeader = authorizationHeader != null && authorizationHeader.startsWith(properties.tokenPrefix)

        var token: String? = null
        var username: String? = null
        if (isValidHeader) {
            try {
                token = authorizationHeader!!.replace(properties.tokenPrefix, "").trim()
                username = utils.parseUsername(token)
            } catch (iae: IllegalArgumentException) {
                logger.warn("An error occurred during getting username from token", iae)
            } catch (eje: ExpiredJwtException) {
                logger.warn("Token is expired and not valid anymore", eje)
            } catch (se: SignatureException) {
                logger.warn("Authentication Failed. Username or Password aren't valid")
            }
        } else {
            logger.warn("Couldn't find authorization token with Bearer")
        }

        if (username != null && token != null && SecurityContextHolder.getContext().authentication == null) {
            val userDetails = userDetailsService.loadUserByUsername(username)
            if (utils.isValidToken(token, username)) {
                val authentication = UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.authorities
                )

                authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                logger.info("Authenticated user $username, setting security context")
                SecurityContextHolder.getContext().authentication = authentication
            }
        }

        filterChain.doFilter(request, response)
    }
}