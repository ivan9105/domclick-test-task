package com.domclick.config.security.ldap.repository

import com.domclick.config.security.ldap.entry.User
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.data.ldap.repository.LdapRepository
import org.springframework.stereotype.Repository

@ConditionalOnProperty(name = ["ldap.persistence.enable"], havingValue = "true")
@Repository
interface LdapUserRepository : LdapRepository<User> {
    fun findByUsername(username: String): User?

    fun findByUsernameAndPassword(username: String, decodedPassword: String): User?

    fun findByUsernameLikeIgnoreCase(username: String): List<User>
}