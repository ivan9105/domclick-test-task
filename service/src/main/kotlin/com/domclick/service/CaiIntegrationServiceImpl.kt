package com.domclick.service

import mu.KLogging
import org.springframework.stereotype.Service
import java.lang.Thread.sleep
import java.time.LocalDate
import java.util.*
import java.util.concurrent.TimeUnit.MINUTES

@Service
class CaiIntegrationServiceImpl : CaiIntegrationService {

    companion object : KLogging()

    override fun fetchEvents(startDate: LocalDate, endDate: LocalDate) {
        val random = Random()

        try {
            sleep(MINUTES.toMillis(random.nextInt(2).toLong()))
        } catch (ie: InterruptedException) {
            logger.error(ie) { "Error when fetch events" }
        }
    }
}