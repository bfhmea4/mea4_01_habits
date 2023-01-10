package ch.bfh.habits.config

import ch.bfh.habits.filters.JwtRequestFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfiguration(@Autowired private val jwtRequestFilter: JwtRequestFilter) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain? {
        http.
            authorizeRequests { authorizeRequests ->
                authorizeRequests
                    .antMatchers("/api/register", "/api/login", "/api/docs/**", "/api/swagger-ui/**").permitAll()
                    .anyRequest().authenticated()
                    .and().sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .csrf().disable()
            .cors(Customizer.withDefaults())
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter::class.java)
            .exceptionHandling().authenticationEntryPoint(HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
        return http.build()
    }

    @Bean
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager? {
        return authenticationConfiguration.authenticationManager
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}
