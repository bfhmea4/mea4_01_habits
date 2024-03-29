package ch.bfh.habits.config

import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component

@Component
class ConfigurationGuard : InitializingBean {
    @Autowired
    private val environment: Environment? = null

    @Value("\${allowed.origins:#{null}}")
    private var allowedOrigins: String? = null

    @Value("\${jwt.signing.key:#{null}}")
    private var jwtSigningKey: String? = null

    @Value("\${jwt.token.validity:#{null}}")
    private var jwtTokenValidity: Int? = null

    override fun afterPropertiesSet() {
        if (allowedOrigins == null) {
            throw IllegalArgumentException("\${allowed.origins} must be configured")
        }

        if (environment == null || !environment.activeProfiles.contains("local")) {
            if (jwtSigningKey == "CHANGEME") {
                throw IllegalArgumentException("\${jwt.signing.key} must be configured with an env variable in production")
            }
            if (jwtTokenValidity == 1000) {
                throw IllegalArgumentException("\${jwt.token.validity} must be configured with an env variable in production")
            }
        }
    }
}
