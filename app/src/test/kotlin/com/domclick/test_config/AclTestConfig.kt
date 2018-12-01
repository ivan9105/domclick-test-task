package com.domclick.test_config

import com.domclick.config.FlywayConfig
import com.domclick.config.security.acl.AclConfig
import com.domclick.config.security.acl.AclMethodSecurityConfig
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.*
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@PropertySource("classpath:application-test.properties")
@ComponentScan(basePackages = ["com.domclick.config.security.acl"])
@Profile("test")
@EnableTransactionManagement
@EnableAutoConfiguration
@EntityScan(value = ["com.domclick.model.acl"])
@EnableJpaRepositories(basePackages = ["com.domclick.repository.acl"])
@Import(AclConfig::class, AclMethodSecurityConfig::class, FlywayConfig::class)
class AclTestConfig