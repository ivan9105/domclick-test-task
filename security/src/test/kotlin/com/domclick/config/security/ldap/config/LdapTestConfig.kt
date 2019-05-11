package com.domclick.config.security.ldap.config

import com.domclick.config.security.ldap.LdapConfig
import com.domclick.config.security.ldap.LdapSecurityProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.*
import org.springframework.core.io.ResourceLoader
import org.springframework.ldap.test.TestContextSourceFactoryBean

@Configuration
@PropertySource("classpath:application-ldap-test.properties")
@ComponentScan(basePackages = ["com.domclick.config.security.ldap.*"], basePackageClasses = [LdapSecurityProperties::class])
@Import(LdapConfig::class)
@Profile("ldap-test")
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