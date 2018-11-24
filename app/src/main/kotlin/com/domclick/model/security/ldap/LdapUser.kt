package com.domclick.model.security.ldap

import org.springframework.ldap.odm.annotations.Attribute
import org.springframework.ldap.odm.annotations.Entry
import org.springframework.ldap.odm.annotations.Id
import javax.naming.Name

@Entry(base = "ou=people", objectClasses = ["top", "person", "organizationalPerson", "inetOrgPerson"])
class LdapUser {
    @Id
    private var id: Name? = null

    @Attribute(name = "cn")
    private var fullName: String? = null
    @Attribute(name = "sn")
    private var name: String? = null
    @Attribute(name = "uid")
    private var uid: String? = null
    @Attribute(name = "userPassword")
    private var password: String? = null
}