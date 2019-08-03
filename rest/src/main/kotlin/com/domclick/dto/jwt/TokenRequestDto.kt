package com.domclick.dto.jwt

data class TokenRequestDto(
        val username: String,
        val password: String
)