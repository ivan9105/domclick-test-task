package com.domclick.rest.handler

import com.domclick.exception.BadRequestException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.util.*

@ControllerAdvice
@RestController
class CustomResponseEntityExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = [(BadRequestException::class)])
    protected fun handleBadRequest(ex: BadRequestException, request: WebRequest): ResponseEntity<Any> {
        val errorAttributes = LinkedHashMap<String, Any>()
        errorAttributes["timestamp"] = Date()
        errorAttributes["status"] = 400
        errorAttributes["error"] = "Bad request error"
        errorAttributes["message"] = ex.message
        errorAttributes["path"] = (request as ServletWebRequest).request.requestURI
        return handleExceptionInternal(ex, errorAttributes, HttpHeaders(), HttpStatus.BAD_REQUEST, request)
    }
}
