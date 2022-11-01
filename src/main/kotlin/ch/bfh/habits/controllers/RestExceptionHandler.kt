package ch.bfh.habits.controllers

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import javax.persistence.EntityNotFoundException

@ControllerAdvice
class RestExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = [EntityNotFoundException::class])
    protected fun handleConflict(ex: Exception?, request: WebRequest?, ): ResponseEntity<Any> {
        val bodyOfResponse = ex?.message
        return handleExceptionInternal(
            ex!!,
            bodyOfResponse,
            HttpHeaders(),
            HttpStatus.NOT_FOUND,
            request!!)
    }
}
