package com.domclick.app

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.ldap.LdapAutoConfiguration
import org.springframework.boot.autoconfigure.ldap.embedded.EmbeddedLdapAutoConfiguration
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication(exclude = [SecurityAutoConfiguration::class, EmbeddedLdapAutoConfiguration::class, LdapAutoConfiguration::class])
@ComponentScan(value = ["com.domclick"])
class RestApplication {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(RestApplication::class.java)
        }
    }
}