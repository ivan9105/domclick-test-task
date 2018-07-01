package com.domclick.rest;

import com.domclick.exception.BadRequestException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = BadRequestException.class)
    protected ResponseEntity<Object> handleConflict(BadRequestException ex, WebRequest request) {
        Map<String, Object> errorAttributes = new LinkedHashMap<>();
        errorAttributes.put("timestamp", new Date());
        errorAttributes.put("status", 400);
        errorAttributes.put("error", "Bad request error");
        errorAttributes.put("message", ex.getMessage());
        errorAttributes.put("path", ((ServletWebRequest) request).getRequest().getRequestURI());
        return handleExceptionInternal(ex, errorAttributes, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}
