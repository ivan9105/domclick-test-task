package com.domclick.config

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.context.annotation.Configuration
import javax.annotation.PostConstruct

@Configuration
class JacksonConfiguration(
        private val objectMapper: ObjectMapper
) {

    @PostConstruct
    fun init() {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        objectMapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, true)
        objectMapper.registerModule(KotlinModule())
    }
}