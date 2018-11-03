package com.domclick.job.listener

import org.apache.commons.logging.LogFactory
import org.quartz.JobExecutionContext
import org.quartz.JobExecutionException
import org.quartz.JobListener
import org.springframework.stereotype.Service

@Service
class JobListenerGenerateReport : JobListener {
    private val logger = LogFactory.getLog(javaClass)

    override fun getName(): String {
        return "Generate Report"
    }

    override fun jobToBeExecuted(context: JobExecutionContext) {
        logger.info("Generate Report Job to be executed " + jobName(context))
    }

    override fun jobWasExecuted(context: JobExecutionContext, jobException: JobExecutionException?) {
        logger.info(
                "Generate Report Job was executed " +
                        jobName(context) +
                        if (jobException != null) ", with error" else ""
        )
    }

    override fun jobExecutionVetoed(context: JobExecutionContext) {
        logger.info("Generate Report Job execution vetoed " + context.jobDetail.key.name)
    }

    private fun jobName(context: JobExecutionContext) = context.jobDetail.key.name
}