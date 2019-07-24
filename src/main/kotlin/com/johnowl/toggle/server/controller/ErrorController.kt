package com.johnowl.toggle.server.controller

import com.johnowl.toggle.server.domain.FeatureToggleAlreadyExistsException
import com.johnowl.toggle.server.domain.FeatureToggleNotFoundException
import com.johnowl.toggle.server.domain.VariablesNotFoundException
import io.swagger.annotations.ApiModel
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ApiModel
data class Error(val code: String, val message: String)

@ControllerAdvice
class ErrorController {

    val logger = LoggerFactory.getLogger(this::class.java.name)

    @ExceptionHandler(FeatureToggleNotFoundException::class)
    fun handleError(exceptionFeature: FeatureToggleNotFoundException): ResponseEntity<Error> {
        return ResponseEntity(Error(
                "toggle_not_found",
                "Feature toggle not found."),
                HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(FeatureToggleAlreadyExistsException::class)
    fun handleError(exception: FeatureToggleAlreadyExistsException): ResponseEntity<Error> {
        return ResponseEntity(Error(
                "toggle_duplicated",
                "Feature toggle already exists."),
                HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(VariablesNotFoundException::class)
    fun handleError(exceptionFeature: VariablesNotFoundException): ResponseEntity<Error> {
        return ResponseEntity(Error(
                "variables_not_found",
                "Variables not found."),
                HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(Exception::class)
    fun handleError(exception: Exception): ResponseEntity<Error> {

        val errorMessage = "An unknown error ocurred."
        logger.error(errorMessage, exception)

        return ResponseEntity(Error(
                "internal_server_error",
                errorMessage),
                HttpStatus.INTERNAL_SERVER_ERROR)
    }
}