package com.domclick.repository.jwt

import com.domclick.entity.jwt.JwtUserEntity
import org.springframework.data.repository.CrudRepository
import java.util.*

interface JwtUserRepository : CrudRepository<JwtUserEntity, Long> {
    fun findByUsername(username: String): Optional<JwtUserEntity>
}