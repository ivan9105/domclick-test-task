package com.domclick.repository.jwt

import com.domclick.entity.jwt.JwtUser
import org.springframework.data.repository.CrudRepository
import java.util.*

interface JwtUserRepository : CrudRepository<JwtUser, Long> {
    fun findByUsername(username: String): Optional<JwtUser>
}