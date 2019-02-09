package com.domclick.config

import org.springframework.beans.factory.config.PropertiesFactoryBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import java.io.IOException
import java.util.*

@Configuration
@EnableAsync
@EnableScheduling
class ScheduleConfig {

    //Todo

    @Bean
    @Throws(IOException::class)
    fun quartzProperties(): Properties? {
        val propertiesFactoryBean = PropertiesFactoryBean()
        propertiesFactoryBean.setLocation(ClassPathResource("/quartz.properties"))
        propertiesFactoryBean.afterPropertiesSet()
        return propertiesFactoryBean.getObject()
    }
}