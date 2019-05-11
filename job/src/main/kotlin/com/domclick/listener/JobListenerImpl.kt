package com.domclick.listener

import com.domclick.job.JobWithParams
import com.domclick.util.getJobInfo
import mu.KLogging
import org.quartz.JobExecutionContext
import org.quartz.JobExecutionException
import org.quartz.JobListener
import org.springframework.stereotype.Service

@Service
class JobListenerImpl : JobListener {
    companion object : KLogging()

    override fun jobExecutionVetoed(context: JobExecutionContext) {
        logger.info { "Job execution '${getJobInfo(context)}' vetoed" }
    }

    override fun jobWasExecuted(context: JobExecutionContext, jobException: JobExecutionException?) {
        if (jobException == null) {
            logger.info { "Job execution '${getJobInfo(context)}' completed" }
        } else {
            logger.error(jobException) { jobException.localizedMessage }
        }
    }

    override fun jobToBeExecuted(context: JobExecutionContext) {
        val jobInstance = context.jobInstance
        logger.info {
            "Job execution '${getJobInfo(context)}' started" +
                    if (jobInstance is JobWithParams) ", with params: ${(jobInstance as JobWithParams).getParamsLocalized(context)}" else ""
        }
    }

    override fun getName() = "Domclick job listener"
}