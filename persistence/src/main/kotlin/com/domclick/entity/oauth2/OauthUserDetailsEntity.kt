package com.domclick.entity.oauth2

import com.domclick.entity.AuthorityEntity
import com.domclick.entity.IdentifierEntity
import org.apache.commons.lang.BooleanUtils.isFalse
import org.apache.commons.lang.BooleanUtils.isTrue
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.*

@Entity
@Table(name = "USER_", uniqueConstraints = [(UniqueConstraint(columnNames = arrayOf("USER_NAME")))])
class OauthUserDetailsEntity : IdentifierEntity(), UserDetails {
    @Column(name = "USER_NAME")
    private var username: String? = null

    @Column(name = "PASSWORD")
    private var password: String? = null

    @Column(name = "ACCOUNT_EXPIRED")
    var accountExpired: Boolean? = null

    @Column(name = "ACCOUNT_LOCKED")
    var accountLocked: Boolean? = null

    @Column(name = "CREDENTIALS_EXPIRED")
    var credentialsExpired: Boolean? = null

    @Column(name = "ENABLED")
    var enabled: Boolean? = null

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "USERS_AUTHORITIES",
            joinColumns = [(JoinColumn(name = "USER_ID", referencedColumnName = "ID"))],
            inverseJoinColumns = [(JoinColumn(name = "AUTHORITY_ID", referencedColumnName = "ID"))])
    @OrderBy
    private var authorities: Collection<AuthorityEntity> = mutableSetOf()

    override fun getAuthorities() = authorities

    override fun isEnabled() = isTrue(enabled)

    override fun getUsername() = username

    override fun isCredentialsNonExpired() = isFalse(credentialsExpired)

    override fun getPassword() = password

    override fun isAccountNonExpired() = isFalse(accountExpired)

    override fun isAccountNonLocked() = isFalse(accountLocked)
}