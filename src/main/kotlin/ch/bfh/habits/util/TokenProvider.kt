package ch.bfh.habits.util

import ch.bfh.habits.entities.User
import ch.bfh.habits.exceptions.UnauthorizedException
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.*

@Component
class TokenProvider {
    @Value("\${jwt.token.validity}")
    var tokenValidity: Long = 0

    @Value("\${jwt.signing.key}")
    var signingKey: String? = null

    @Value("\${jwt.token.prefix}")
    var tokenPrefix: String = ""

    fun extractUsername(token: String): String {
        return extractAllClaims(token).subject
    }

    fun extractId(token: String): Long {
        return extractAllClaims(token).get("id", Integer::class.java).toLong()
    }

    fun generateToken(userDetails: UserDetails, user: User): String {
        return Jwts.builder()
            .setIssuer("Habits")
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + tokenValidity * 1000)) // 1 week
            .setSubject(userDetails.username)
            .addClaims(mapOf("id" to user.id))
            .addClaims(mapOf("email" to user.email))
            .addClaims(mapOf("firstName" to user.firstName))
            .addClaims(mapOf("lastName" to user.lastName))
            .signWith(SignatureAlgorithm.HS512, signingKey).compact()
    }

    fun validateToken(token: String?, userDetails: UserDetails): Boolean {
        if (token.isNullOrEmpty()) {
            return false
        }

        return try {
            userDetails.username == extractUsername(token)
        } catch (e: Exception) {
            false
        }
    }

    private fun extractAllClaims(token: String): Claims {
        try {
            return Jwts.parser().setSigningKey(signingKey).parseClaimsJws(token.replace(tokenPrefix, "").trim()).body
        } catch (e: Exception) {
            throw UnauthorizedException("Invalid token")
        }
    }
}
