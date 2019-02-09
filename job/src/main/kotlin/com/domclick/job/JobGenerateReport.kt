package com.domclick.job

import com.domclick.service.GenerateReportService
import org.quartz.DisallowConcurrentExecution
import org.quartz.Job
import org.quartz.JobExecutionContext

@DisallowConcurrentExecution
class JobGenerateReport(private val service: GenerateReportService) : Job {


    override fun execute(context: JobExecutionContext?) {
        //Todo
    }
}