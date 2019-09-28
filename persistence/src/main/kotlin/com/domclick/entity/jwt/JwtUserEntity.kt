package com.domclick.entity.jwt

import com.domclick.entity.IdentifierEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "JWT_USER")
class JwtUserEntity : IdentifierEntity() {
        @Column
        var username: String? = null
        @Column
        var password: String? = null
}