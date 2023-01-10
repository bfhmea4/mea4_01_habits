package ch.bfh.habits.filters

import ch.bfh.habits.services.UserService
import ch.bfh.habits.util.TokenProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtRequestFilter @Autowired constructor(
    private val tokenProvider: TokenProvider,
    private val userService: UserService,
) : OncePerRequestFilter() {
    @Value("\${jwt.token.prefix}")
    var tokenPrefix: String = ""

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val authorizationHeader = request.getHeader("Authorization")
        var username: String? = null
        var jwtToken: String? = null

        if (authorizationHeader != null && authorizationHeader.startsWith(tokenPrefix)) {
            jwtToken = authorizationHeader.replace(tokenPrefix, "").trim()
            username = tokenProvider.extractUsername(jwtToken)
        }

        if (username != null && SecurityContextHolder.getContext().authentication == null) {
            val userDetails = userService.loadUserByUsername(username)
            if (tokenProvider.validateToken(jwtToken, userDetails)) {
                val usernamePasswordAuthenticationToken =
                    UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                usernamePasswordAuthenticationToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = usernamePasswordAuthenticationToken
            }
        }

        filterChain.doFilter(request, response)
    }
}
