package com.domclick.repository.jwt

import com.domclick.entity.jwt.JwtUserDetailsEntity
import org.springframework.data.repository.CrudRepository
import java.util.*

interface JwtUserDetailsRepository : CrudRepository<JwtUserDetailsEntity, Long> {
    fun findByUsername(username: String): Optional<JwtUserDetailsEntity>
}