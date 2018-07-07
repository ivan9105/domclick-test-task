package com.domclick.config

import org.flywaydb.core.Flyway
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

import javax.sql.DataSource

@Configuration
class FlywayConfig {
    @Autowired
    internal var dataSource: DataSource? = null

    @Bean
    fun flyway(): Flyway {
        val flyway = Flyway()
        flyway.dataSource = dataSource
        flyway.sqlMigrationPrefix = "V"
        flyway.migrate()
        return flyway
    }
}
