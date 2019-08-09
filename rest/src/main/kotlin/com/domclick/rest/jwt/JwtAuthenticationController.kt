package com.domclick.rest.jwt

import com.domclick.config.security.jwt.util.JwtTokenUtil
import com.domclick.dto.jwt.TokenRequestDto
import com.domclick.dto.jwt.TokenResponseDto
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@ConditionalOnProperty(name = ["security.protocol"], havingValue = "jwt")
@RestController
@RequestMapping("/api/token")
class JwtAuthenticationController(
        private val authenticationManager: AuthenticationManager,
        private val tokenUtils: JwtTokenUtil
) {

    @PostMapping("/generate")
    fun generate(@Valid @RequestBody request: TokenRequestDto) : ResponseEntity<TokenResponseDto> {
        //Todo use advice
        authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                        request.username,
                        request.password
                )
        )

        return ResponseEntity
                .ok<TokenResponseDto>(
                        TokenResponseDto(
                                request.username,
                                tokenUtils.generateToken(request.username)
                        )
                )
    }
}