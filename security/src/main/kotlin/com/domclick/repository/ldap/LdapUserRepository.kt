package com.domclick.repository.ldap

import com.domclick.entity.security.ldap.LdapUser
import org.springframework.data.ldap.repository.LdapRepository
//import org.springframework.data.ldap.repository.LdapRepository
import org.springframework.stereotype.Repository

@Repository
interface LdapUserRepository : LdapRepository<LdapUser> {

    fun findByFullName(fullName: String): LdapUser?

    fun findByName(name: String): LdapUser?

    fun findByUid(uid: String): LdapUser?

    fun findByPassword(password: String): LdapUser?
}