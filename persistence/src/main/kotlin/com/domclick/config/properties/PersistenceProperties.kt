package com.domclick.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import javax.validation.constraints.NotBlank

@Configuration
@ConfigurationProperties(prefix = "persistance")
class PersistenceProperties {
    @NotBlank
    lateinit var vendor: String
}