package com.domclick.config.security.ldap.service

import com.domclick.config.security.ldap.client.LdapClient.Companion.OBJECT_CLASSES
import com.domclick.config.security.ldap.entry.User
import com.domclick.config.security.ldap.repository.LdapUserRepository
import com.domclick.config.security.ldap.util.digestSHA
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.ldap.support.LdapNameBuilder
import org.springframework.stereotype.Service
import javax.persistence.EntityNotFoundException

@ConditionalOnProperty(name = ["ldap.persistence.enable"], havingValue = "true")
@Service
class UserService(
        private val ldapUserRepository: LdapUserRepository
) {
    fun authenticate(username: String, password: String) =
            ldapUserRepository.findByUsernameAndPassword(username, password) != null

    fun search(username: String): List<String> {
        val result = ldapUserRepository.findByUsernameLikeIgnoreCase(username)
        if (result.isEmpty()) {
            return listOf()
        }
        return result.map { it.username }.toList()
    }

    fun create(username: String, password: String): User {
        val newUser = User().apply {
            id = LdapNameBuilder.newInstance()
                    .build()
            this.username = username
            this.password = password
            this.encodePassword = digestSHA(password)
            this.objectClasses = OBJECT_CLASSES.toList()
        }

        return ldapUserRepository.save(newUser)
    }

    fun update(username: String, password: String) {
        val user = ldapUserRepository.findByUsername(username)
        user ?: throw EntityNotFoundException("Entry user with username '$username' not found")
        user.password = digestSHA(password)
        ldapUserRepository.save(user)
    }
}