package com.domclick.config.security.jwt.service

import com.domclick.entity.jwt.JwtUserDetailsEntity

interface JwtUserService {
    fun save(username: String, password: String): JwtUserDetailsEntity
}