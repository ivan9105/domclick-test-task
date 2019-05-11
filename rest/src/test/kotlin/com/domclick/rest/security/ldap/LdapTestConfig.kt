package com.domclick.config.security.ldap.config

import com.domclick.config.security.ldap.LdapSecurityProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ResourceLoader
import org.springframework.ldap.test.TestContextSourceFactoryBean

@ConditionalOnExpression("'\${ldap.persistence.enable}'=='true' or '\${security.protocol}'=='ldap'")
@Configuration
class LdapTestConfig {
    @Autowired
    lateinit var properties: LdapSecurityProperties

    @Autowired
    lateinit var resourceLoader: ResourceLoader

    @Bean
    fun testContextSource() =
            TestContextSourceFactoryBean().apply {
                setDefaultPartitionName(properties.partition)
                setDefaultPartitionSuffix(properties.partitionSuffix)
                setPrincipal(properties.principal)
                setPassword(properties.password)
                setLdifFile(resourceLoader.getResource(properties.ldiffile))
                setPort(properties.port)
            }
}