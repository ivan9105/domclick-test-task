package com.domclick.config.security.ldap.util

import com.domclick.config.security.ldap.client.LdapClient
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

/**
 * Decode in base64 with SHA digest
 * in test.ldif keep userPassword encoded with base64
 */
fun digestSHA(password: String): String {
    val base64: String?
    try {
        val digest = MessageDigest.getInstance(LdapClient.DIGEST_ALGORITHM)
        digest.update(password.toByteArray(StandardCharsets.UTF_8))
        base64 = Base64
                .getEncoder()
                .encodeToString(digest.digest())
    } catch (e: NoSuchAlgorithmException) {
        throw RuntimeException(e)
    }
    return "{SHA}$base64"
}