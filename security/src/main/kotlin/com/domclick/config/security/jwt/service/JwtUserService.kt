package com.domclick.config.security.jwt.service

import com.domclick.entity.jwt.JwtUserEntity

interface JwtUserService {
    fun save(username: String, password: String): JwtUserEntity
}