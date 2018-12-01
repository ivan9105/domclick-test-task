package com.domclick.config

import org.flywaydb.core.Flyway
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
class FlywayConfig(private val dataSource_: DataSource) {

    @Bean
    fun flyway() = Flyway().apply {
        this.dataSource = dataSource_
        sqlMigrationPrefix = "V"
        migrate()
    }
}
