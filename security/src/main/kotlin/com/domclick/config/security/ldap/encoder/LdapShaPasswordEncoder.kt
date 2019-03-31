package com.domclick.config.security.ldap.encoder

import com.domclick.config.security.ldap.util.digestSHA
import org.springframework.security.crypto.password.PasswordEncoder

class LdapShaPasswordEncoder : PasswordEncoder {
    override fun encode(rawPassword: CharSequence) = digestSHA(rawPassword as String)

    override fun matches(rawPassword: CharSequence?, encodedPassword: String) =
            if (rawPassword != null) digestSHA(rawPassword as String) == encodedPassword else false
}