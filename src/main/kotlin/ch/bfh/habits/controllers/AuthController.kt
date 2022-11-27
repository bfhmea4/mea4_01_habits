package ch.bfh.habits.controllers

import ch.bfh.habits.dtos.ErrorMessage
import ch.bfh.habits.dtos.JwtToken
import ch.bfh.habits.dtos.user.LoginDTO
import ch.bfh.habits.dtos.user.RegisterDTO
import ch.bfh.habits.dtos.user.UserEntityBuilder
import ch.bfh.habits.entities.User
import ch.bfh.habits.services.CustomUserDetailService
import ch.bfh.habits.services.UserService
import ch.bfh.habits.util.TokenProvider
import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.servlet.http.HttpServletResponse

@RestController
class AuthController @Autowired constructor(
        private val authenticationManager: AuthenticationManager,
        private val tokenProvider: TokenProvider,
        private val userService: UserService,
        private val customUserDetailService: CustomUserDetailService
    ) {
    @PostMapping("api/register")
    fun register(@RequestBody body: RegisterDTO): ResponseEntity<User> {
        val user = UserEntityBuilder.createUserEntityFromDTO(body)
        return ResponseEntity.ok(this.userService.save(user))
    }

    @PostMapping("api/login")
    fun login(@RequestBody body: LoginDTO, response: HttpServletResponse): ResponseEntity<Any> {
         try {
             authenticationManager.authenticate(UsernamePasswordAuthenticationToken(body.userName, body.password))
         } catch (e: BadCredentialsException) {
             throw Exception("Incorrect username or password")
         }

        val userDetails = customUserDetailService.loadUserByUsername(body.userName)
        val jwt = tokenProvider.generateToken(userDetails, this.userService.findByUserName(body.userName)!!)

        return ResponseEntity.ok(JwtToken(jwt))
    }

    @GetMapping("api/user")
    fun user(@RequestHeader(value = "Authorization") token: String?): ResponseEntity<Any> {
        try {
            if (token == null) {
                return ResponseEntity.status(401).body(ErrorMessage("Unauthenticated"))
            }

            val body = Jwts.parser().setSigningKey("secret").parseClaimsJws(token.split(' ').last()).body

            return ResponseEntity.ok(this.userService.getById((body["id"] as Int).toLong()))
        } catch (e: Exception) {
            return ResponseEntity.status(401).body(ErrorMessage("Unauthenticated"))
        }
    }
}
