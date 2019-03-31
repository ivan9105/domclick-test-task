package com.domclick.config.security.ldap.client

import com.domclick.config.security.ldap.LdapSecurityProperties
import com.domclick.config.security.ldap.util.digestSHA
import org.springframework.ldap.core.*
import org.springframework.ldap.support.LdapNameBuilder

class LdapClient(
        private val contextSource: ContextSource,
        private val ldapTemplate: LdapTemplate,
        private val properties: LdapSecurityProperties
) {
    companion object {
        const val DIGEST_ALGORITHM = "SHA"
        const val GROUP_NAME_ATTR = "ou"
        const val USER_NAME_ATTR = "cn"
        const val USER_PASSWORD_NAME_ATTR = "sn"
        const val OBJECT_NAME_ATTR = "objectclass"
        const val USER_PASSWORD_ENCODED_NAME_ATTR = "userPassword"

        const val USER_GROUP_NAME = "users"

        const val TOP_CLASS_NAME = "top"
        const val PERSON_CLASS_NAME = "person"
        private const val ORGANIZATIONAL_PERSON_CLASS_NAME = "organizationalPerson"
        const val INET_ORG_PERSON = "inetOrgPerson"

        val OBJECT_CLASSES = arrayOf(TOP_CLASS_NAME, PERSON_CLASS_NAME, ORGANIZATIONAL_PERSON_CLASS_NAME, INET_ORG_PERSON)

        const val BASE = "$GROUP_NAME_ATTR=$USER_GROUP_NAME"
    }

    fun authenticate(username: String, password: String) =
            contextSource.getContext(
                    "$USER_NAME_ATTR=$username,$BASE,${properties.partitionSuffix}",
                    password
            )!!

    fun search(username: String): List<String>? =
            ldapTemplate.search(
                    BASE,
                    "$USER_NAME_ATTR=$username",
                    AttributesMapper<String> { attributes ->
                        attributes.get(USER_NAME_ATTR).get() as String
                    }
            )

    fun create(username: String, password: String) {
        val dn = buildDirectory(username)
        val context = DirContextAdapter(dn)
        fillContext(context, username, password)
        ldapTemplate.bind(context)
    }

    fun modify(username: String, password: String) {
        val dn = buildDirectory(username)
        val context = ldapTemplate.lookupContext(dn)
        fillContext(context, username, password)
        ldapTemplate.modifyAttributes(context)
    }

    private fun buildDirectory(username: String) =
            LdapNameBuilder
                    .newInstance()
                    .add(GROUP_NAME_ATTR, USER_GROUP_NAME)
                    .add(USER_NAME_ATTR, username)
                    .build()

    private fun fillContext(context: DirContextOperations, username: String, password: String) {
        context.setAttributeValues(OBJECT_NAME_ATTR, OBJECT_CLASSES)
        context.setAttributeValue(USER_NAME_ATTR, username)
        context.setAttributeValue(USER_PASSWORD_NAME_ATTR, username)
        context.setAttributeValue(USER_PASSWORD_ENCODED_NAME_ATTR, digestSHA(password))
    }
}