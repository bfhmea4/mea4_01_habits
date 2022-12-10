package ch.bfh.habits.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.lang.RuntimeException

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
class UnauthorizedException(msg: String): RuntimeException(msg)
