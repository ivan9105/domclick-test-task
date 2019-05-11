package com.domclick.service

import com.domclick.config.JobFactoryImpl
import com.domclick.listener.JobListenerImpl
import org.springframework.beans.factory.config.PropertiesFactoryBean
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.core.io.ClassPathResource
import org.springframework.scheduling.quartz.SchedulerFactoryBean

@TestConfiguration
class ScheduleTestConfig {

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
    fun schedulerFactory(jobFactory: JobFactoryImpl) =
            SchedulerFactoryBean().apply {
                setJobFactory(jobFactory)
                setQuartzProperties(quartzProperties())
                setGlobalJobListeners(JobListenerImpl())
            }

    @Bean
    fun jobManager(schedulerFactory: SchedulerFactoryBean) = JobManager(schedulerFactory)

    @Bean
    fun caiIntegrationService() = CaiIntegrationServiceImpl()
}