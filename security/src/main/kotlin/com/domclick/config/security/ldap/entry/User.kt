package com.domclick.config.security.ldap.entry

import com.domclick.config.security.ldap.client.LdapClient.Companion.BASE
import com.domclick.config.security.ldap.client.LdapClient.Companion.INET_ORG_PERSON
import com.domclick.config.security.ldap.client.LdapClient.Companion.OBJECT_NAME_ATTR
import com.domclick.config.security.ldap.client.LdapClient.Companion.PERSON_CLASS_NAME
import com.domclick.config.security.ldap.client.LdapClient.Companion.TOP_CLASS_NAME
import com.domclick.config.security.ldap.client.LdapClient.Companion.USER_NAME_ATTR
import com.domclick.config.security.ldap.client.LdapClient.Companion.USER_PASSWORD_ENCODED_NAME_ATTR
import com.domclick.config.security.ldap.client.LdapClient.Companion.USER_PASSWORD_NAME_ATTR
import org.springframework.ldap.odm.annotations.Attribute
import org.springframework.ldap.odm.annotations.Entry
import org.springframework.ldap.odm.annotations.Id
import javax.naming.Name

@Entry(base = BASE, objectClasses = [PERSON_CLASS_NAME, INET_ORG_PERSON, TOP_CLASS_NAME])
class User {
    @Id
    lateinit var id: Name
    @Attribute(name = USER_NAME_ATTR)
    lateinit var username: String
    @Attribute(name = USER_PASSWORD_NAME_ATTR)
    lateinit var password: String
    @Attribute(name = USER_PASSWORD_ENCODED_NAME_ATTR)
    lateinit var encodePassword: String
    @Attribute(name = OBJECT_NAME_ATTR)
    lateinit var objectClasses: List<String>
}