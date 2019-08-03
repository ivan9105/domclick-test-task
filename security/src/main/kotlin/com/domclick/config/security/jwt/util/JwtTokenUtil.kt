package com.domclick.config.security.jwt.util

import com.domclick.config.security.jwt.properties.JwtSecurityProperties
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import java.lang.System.currentTimeMillis
import java.util.*
import java.util.Arrays.asList

@ConditionalOnProperty(name = ["security.protocol"], havingValue = "jwt")
@Component
class JwtTokenUtil(
        private val properties: JwtSecurityProperties
) {
    companion object {
        private val SCOPES_PARAM = "scopes"
    }

    fun generateToken(username: String) : String {
        val claims = Jwts.claims().setSubject(username)
        claims[SCOPES_PARAM] = mockAuthority()
        val currentTimeMillis = currentTimeMillis()

        return Jwts.builder()
                .setClaims(claims)
                .setIssuer("http://domclick.com")
                .setIssuedAt(Date(currentTimeMillis))
                .setExpiration(Date(currentTimeMillis + properties.accessTokenThresholdInSec * 1000))
                .signWith(SignatureAlgorithm.HS256, properties.signingKey)
                .compact()
    }

    fun parseUsername(token: String) = parseClaims(token).subject!!

    fun isValidToken(token: String, username: String) = parseUsername(token).equals(username) && !isTokenExpired(token)

    private fun isTokenExpired(token: String) = parseClaims(token).expiration.before(Date())

    private fun mockAuthority() = asList(SimpleGrantedAuthority("ROLE_ADMIN"))

    private fun parseClaims(token: String) =
            Jwts.parser()
                    .setSigningKey(properties.signingKey)
                    .parseClaimsJws(token)
                    .body
}