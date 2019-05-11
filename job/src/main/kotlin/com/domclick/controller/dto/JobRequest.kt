package com.domclick.controller.dto

import com.domclick.config.deserializer.JobRequestDeserializer
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.PositiveOrZero

@JsonDeserialize(using = JobRequestDeserializer::class)
data class JobRequest(
        @NotBlank(message = "Field 'type' can not be blank")
        val type: String,

        @NotNull(message = "Field 'intervalInSeconds' is required")
        @PositiveOrZero(message = "Field 'intervalInSeconds' value must be positive")
        val intervalInSeconds: Int = 0,

        val params: Map<String, Any>
)