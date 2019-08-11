package com.domclick.entity

import org.springframework.security.core.GrantedAuthority
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table
import javax.persistence.UniqueConstraint

@Entity
@Table(name = "AUTHORITY", uniqueConstraints = [(UniqueConstraint(columnNames = arrayOf("NAME")))])
class AuthorityEntity : IdentifierEntity(), GrantedAuthority {

    @Column(name = "NAME")
    private val name: String? = null

    override fun getAuthority() = name
}