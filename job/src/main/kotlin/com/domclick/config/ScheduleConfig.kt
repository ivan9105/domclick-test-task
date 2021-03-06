package com.domclick.config

import com.domclick.listener.JobListenerImpl
import org.springframework.beans.factory.config.PropertiesFactoryBean
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.quartz.SchedulerFactoryBean
import javax.sql.DataSource

@Configuration
@EnableAsync
@EnableScheduling
class ScheduleConfig {

    @Bean
    fun jobFactory(context: ApplicationContext) = JobFactoryImpl().apply {
        setApplicationContext(context)
    }

    @Bean
    fun quartzProperties() =
            PropertiesFactoryBean().apply {
                setLocation(ClassPathResource("/quartz.properties"))
                afterPropertiesSet()
            }.getObject()!!

    @Bean
    fun schedulerFactory(datasource: DataSource, jobFactory: JobFactoryImpl, listener: JobListenerImpl) =
            SchedulerFactoryBean().apply {
                setDataSource(datasource)
                setJobFactory(jobFactory)
                setQuartzProperties(quartzProperties())
                setGlobalJobListeners(listener)
//                isAutoStartup = false
            }
}