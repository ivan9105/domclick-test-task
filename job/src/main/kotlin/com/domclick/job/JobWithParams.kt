package com.domclick.job

import org.quartz.JobExecutionContext

interface JobWithParams {
    fun getParamsLocalized(context: JobExecutionContext) : String
}