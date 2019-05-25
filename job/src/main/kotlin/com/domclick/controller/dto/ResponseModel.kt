package com.domclick.controller.dto

data class ResponseModel<out T>(
        val success: Boolean = true,
        val data: T?
)