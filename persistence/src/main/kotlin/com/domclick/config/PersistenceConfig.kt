package com.domclick.config

import org.flywaydb.core.Flyway
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.annotation.PostConstruct

@Configuration
@EntityScan(value = ["com.domclick.entity"])
@EnableJpaRepositories("com.domclick.repository")
@EnableTransactionManagement
class PersistenceConfig {
    @Autowired
    lateinit var flyway: Flyway

    /**
     * problem with h2
     */
    @PostConstruct
    fun migrate() {
        flyway.clean()
        flyway.migrate()
    }
}