package com.domclick.util

import org.quartz.JobExecutionContext

fun getJobId(context: JobExecutionContext) = context.jobDetail.key.name!!

fun getJobParams(context: JobExecutionContext) = context.jobDetail.jobDataMap!!

fun getJobDescription(context: JobExecutionContext) = context.jobDetail.description ?: ""

fun getJobInfo(context: JobExecutionContext) = "${getJobId(context)}: ${getJobDescription(context)}"