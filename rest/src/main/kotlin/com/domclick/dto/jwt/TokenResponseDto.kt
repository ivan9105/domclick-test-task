package com.domclick.dto.jwt

data class TokenResponseDto(
        val username: String,
        val token: String
)