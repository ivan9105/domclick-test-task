package com.domclick.config

import org.modelmapper.ModelMapper
import org.modelmapper.config.Configuration.AccessLevel.PRIVATE
import org.modelmapper.convention.MatchingStrategies.STRICT
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ModelMapperConfig {

    @Bean
    fun mapper() = ModelMapper().apply {
        configuration.matchingStrategy = STRICT
        configuration.isFieldMatchingEnabled = true
        configuration.isSkipNullEnabled = true
        configuration.fieldAccessLevel = PRIVATE
    }
}