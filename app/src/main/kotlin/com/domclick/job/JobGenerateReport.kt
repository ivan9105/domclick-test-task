package com.domclick.job

import com.domclick.service.GenerateReportService
import org.quartz.DisallowConcurrentExecution
import org.quartz.Job
import org.quartz.JobExecutionContext

@DisallowConcurrentExecution
class JobGenerateReport(private val service: GenerateReportService) : Job {


    override fun execute(context: JobExecutionContext?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}