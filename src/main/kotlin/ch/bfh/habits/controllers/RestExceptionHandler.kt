package ch.bfh.habits.controllers

import ch.bfh.habits.exceptions.BadRequestException
import ch.bfh.habits.exceptions.EntityNotFoundException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class RestExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = [EntityNotFoundException::class])
    protected fun handleConflictEntityNotFound(ex: Exception?, request: WebRequest?): ResponseEntity<Any> {
        val bodyOfResponse = ex?.message
        return handleExceptionInternal(
            ex!!,
            bodyOfResponse,
            HttpHeaders(),
            HttpStatus.NOT_FOUND,
            request!!)
    }

    @ExceptionHandler(value = [BadRequestException::class])
    protected fun handleConflictBadRequest(ex: Exception?, request: WebRequest?): ResponseEntity<Any> {
        val bodyOfResponse = ex?.message
        return handleExceptionInternal(
            ex!!,
            bodyOfResponse,
            HttpHeaders(),
            HttpStatus.BAD_REQUEST,
            request!!)
    }
}
