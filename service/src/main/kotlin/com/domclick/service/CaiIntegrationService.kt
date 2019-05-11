package com.domclick.service

import java.time.LocalDate

/**
 * Some integration with external system
 */
interface CaiIntegrationService {
    /**
     * Emulate workload
     */
    fun fetchEvents(startDate: LocalDate, endDate: LocalDate)
}