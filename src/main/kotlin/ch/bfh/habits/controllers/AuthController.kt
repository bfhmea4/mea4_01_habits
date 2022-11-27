package ch.bfh.habits.controllers

import ch.bfh.habits.dtos.ErrorMessage
import ch.bfh.habits.dtos.JwtToken
import ch.bfh.habits.dtos.user.LoginDTO
import ch.bfh.habits.dtos.user.RegisterDTO
import ch.bfh.habits.dtos.user.UserEntityBuilder
import ch.bfh.habits.entities.User
import ch.bfh.habits.services.UserService
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.servlet.http.HttpServletResponse

@RestController
class AuthController @Autowired constructor(private val userService: UserService) {
    @PostMapping("api/register")
    fun register(@RequestBody body: RegisterDTO): ResponseEntity<User> {
        val user = UserEntityBuilder.createUserEntityFromDTO(body)
        return ResponseEntity.ok(this.userService.save(user))
    }

    @PostMapping("api/login")
    fun login(@RequestBody body: LoginDTO, response: HttpServletResponse): ResponseEntity<Any> {
        val user = if (body.email != null) {
            this.userService.findByEmail(body.email)
        } else if (body.userName != null) {
            this.userService.findByUserName(body.userName)
        } else {
            null
        }

        if (user == null || !user.comparePassword(body.password)) {
            return ResponseEntity.badRequest().body(ErrorMessage("Wrong credentials!"))
        }

        val jwt = Jwts.builder()
            .setIssuer("Habits")
            .setExpiration(Date(System.currentTimeMillis() + 60 * 24 * 1000)) // 1 day
            .addClaims(mapOf("id" to user.id))
            .addClaims(mapOf("email" to user.email))
            .addClaims(mapOf("firstName" to user.firstName))
            .addClaims(mapOf("lastName" to user.lastName))
            .signWith(SignatureAlgorithm.HS512, "secret").compact() //ToDo use env variable

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
