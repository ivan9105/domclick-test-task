package com.domclick.service

import com.domclick.job.CaiFetchEventsJob
import org.quartz.*
import org.quartz.JobKey.jobKey
import org.quartz.impl.matchers.GroupMatcher
import org.springframework.scheduling.quartz.SchedulerFactoryBean
import org.springframework.stereotype.Service
import java.util.UUID.randomUUID

@Service
class JobManager(
        schedulerFactory: SchedulerFactoryBean
) {
    val scheduler = schedulerFactory.scheduler

    fun add(type: String, intervalInSeconds: Int, params: Map<String, Any>): JobInfo {
        var jobClass: Class<out Job>? = null
        if (type == CaiFetchEventsJob.TYPE) {
            jobClass = CaiFetchEventsJob::class.java
        }

        if (jobClass == null) {
            throw IllegalArgumentException("Job with type '$type' not found")
        }

        val id = randomUUID().toString()

        val job = buildJob(jobClass, id, type).apply { jobDataMap.putAll(params) }
        val trigger = buildTrigger(id, type, buildSchedule(intervalInSeconds))

        scheduler.scheduleJob(job, trigger)
        return JobInfo(id, type, scheduler.getTriggerState(trigger!!.key))
    }

    fun delete(id: String, type: String?) = scheduler.deleteJob(if (type == null) jobKey(id) else jobKey(id, type))

    fun find(type: String?, ids: List<String>?) =
            scheduler
                    .getJobKeys(if (!type.isNullOrEmpty()) GroupMatcher.groupEquals(type) else GroupMatcher.anyGroup())
                    .map { scheduler.getJobDetail(it) }
                    .flatMap { scheduler.getTriggersOfJob(it.key) }
                    .map { JobInfo(it.key.name, it.key.group, scheduler.getTriggerState(it.key)) }
                    .toList()

    private fun buildTrigger(id: String, type: String, scheduleBuilder: SimpleScheduleBuilder?): SimpleTrigger? {
        return TriggerBuilder
                .newTrigger()
                .withIdentity(id, type)
                .startNow()
                .withSchedule(
                        scheduleBuilder
                )
                .build()!!
    }

    private fun buildSchedule(intervalInSeconds: Int): SimpleScheduleBuilder {
        return SimpleScheduleBuilder
                .simpleSchedule()
                .withIntervalInSeconds(intervalInSeconds)!!
    }

    private fun buildJob(jobClass: Class<out Job>?, id: String, type: String): JobDetail {
        return JobBuilder
                .newJob(jobClass)
                .withIdentity(id, type)
                .requestRecovery(true)
                .build()!!
    }
}