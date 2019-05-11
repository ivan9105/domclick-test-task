package com.domclick.config

import org.quartz.spi.TriggerFiredBundle
import org.springframework.beans.factory.config.AutowireCapableBeanFactory
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.scheduling.quartz.SpringBeanJobFactory

class JobFactoryImpl : SpringBeanJobFactory(), ApplicationContextAware {
    lateinit var beanFactory: AutowireCapableBeanFactory

    override fun setApplicationContext(context: ApplicationContext) {
        beanFactory = context.autowireCapableBeanFactory
    }

    override fun createJobInstance(bundle: TriggerFiredBundle) = super.createJobInstance(bundle).apply {
        beanFactory.autowireBean(this)
    }
}