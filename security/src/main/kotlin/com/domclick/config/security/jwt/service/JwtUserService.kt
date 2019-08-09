package com.domclick.config.security.jwt.service

import com.domclick.entity.jwt.JwtUser

interface JwtUserService {
    fun save(username: String, password: String): JwtUser
}