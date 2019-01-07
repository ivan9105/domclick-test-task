package com.domclick.config

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@EntityScan(value = ["com.domclick.entity"])
@EnableJpaRepositories("com.domclick.repository")
@EnableTransactionManagement
class PersistenceConfig