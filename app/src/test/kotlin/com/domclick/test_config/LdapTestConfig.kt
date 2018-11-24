package com.domclick.test_config

import org.springframework.context.annotation.*
import org.springframework.data.ldap.repository.config.EnableLdapRepositories

@Configuration
@PropertySource("classpath:application-test.properties")
@ComponentScan(basePackages = [
    "com.domclick.config.properties.ldap.*",
    "com.domclick.config.security.ldap.*",
    "com.domclick.repository.ldap.*",
    "com.domclick.config.security.encryption.*"
])
@EnableLdapRepositories(basePackages = ["com.domclick.repository.ldap.**"])
@Profile("test")
@Import()
class LdapTestConfig