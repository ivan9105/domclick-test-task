package com.domclick.config

import com.domclick.config.properties.PersistenceProperties
import org.flywaydb.core.Flyway
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
class FlywayConfig(
        private val dataSource_: DataSource,
        private val persistenceProperties: PersistenceProperties
) {
    //Todo support migration from jar
    @Bean
    fun flyway() = Flyway().apply {
        this.dataSource = dataSource_
        sqlMigrationPrefix = "V"
        setLocations("db.migration.${persistenceProperties.vendor}")
        migrate()
    }
}
