package com.domclick.service

import org.quartz.Trigger.TriggerState

data class JobInfo(
        val id: String,
        val type: String,
        val state: TriggerState
)