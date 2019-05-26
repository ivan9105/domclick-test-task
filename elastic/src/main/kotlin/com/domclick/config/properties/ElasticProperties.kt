package com.domclick.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive

@Configuration
@ConfigurationProperties(prefix = "elasticsearch")
@Validated
class ElasticProperties {
    @NotBlank
    @NotNull
    var host: String = "127.0.0.1"

    @Positive
    var port: Int? = 9300

    @NotBlank
    @NotNull
    var cluster: String = "docker-cluster"


}