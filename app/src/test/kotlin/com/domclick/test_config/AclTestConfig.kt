package com.domclick.test_config

import com.domclick.config.FlywayConfig
import com.domclick.config.properties.PersistenceProperties
import com.domclick.config.security.config.acl.AclConfig
import com.domclick.config.security.config.acl.AclMethodSecurityConfig
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.*
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@PropertySource("classpath:application-test.properties")
@ComponentScan(basePackages = ["com.domclick.config.security.config.acl"])
@Profile("test")
@EnableTransactionManagement
@EnableAutoConfiguration
@EntityScan(value = ["com.domclick.entity.acl"])
@EnableJpaRepositories(basePackages = ["com.domclick.repository.acl"])
@Import(PersistenceProperties::class, AclConfig::class, AclMethodSecurityConfig::class, FlywayConfig::class)
class AclTestConfig