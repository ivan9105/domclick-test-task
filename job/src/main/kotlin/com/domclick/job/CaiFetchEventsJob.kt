package com.domclick.job

import com.domclick.service.CaiIntegrationService
import com.domclick.util.getJobInfo
import com.domclick.util.getJobParams
import mu.KLogging
import org.quartz.DisallowConcurrentExecution
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.quartz.JobExecutionException
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate

@DisallowConcurrentExecution
class CaiFetchEventsJob: Job, JobWithParams {
    companion object : KLogging() {
        const val START_DATE = "startDate"
        const val END_DATE = "endDate"
        const val TYPE = "CAI_FETCH_EVENTS"
    }

    /**
     * Quartz's jobs must have only constructor without parameters
     */
    @Autowired
    lateinit var caiIntegrationService: CaiIntegrationService

    override fun execute(context: JobExecutionContext) {
        val params = getJobParams(context)

        val startDateStr = params[START_DATE]
        val endDateStr = params[END_DATE]
        if (startDateStr == null || endDateStr == null) {
            throw IllegalArgumentException("'$START_DATE' or '$END_DATE' can't be empty")
        }

        val startDate = LocalDate.parse(startDateStr as String)
        val endDate = LocalDate.parse(endDateStr as String)

        try {
            caiIntegrationService.fetchEvents(startDate, endDate)
        } catch (e: Exception) {
            throw JobExecutionException("Job with id '${getJobInfo(context)}' failed", e)
        }
    }

    override fun getParamsLocalized(context: JobExecutionContext): String {
        val params = getJobParams(context)

        val startDate = params[START_DATE]
        val endDate = params[END_DATE]

        return "start date '$startDate', " +
                "end date '$endDate'"
    }
}