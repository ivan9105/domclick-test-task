package com.domclick.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class FlywayConfig {
    @Autowired
    DataSource dataSource;

    @Bean
    public Flyway flyway(){
        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.setSqlMigrationPrefix("V");
        flyway.migrate();
        return  flyway;

    }
}
