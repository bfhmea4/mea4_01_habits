package ch.bfh.habits.config

import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class ConfigurationGuard : InitializingBean {
    @Value("\${allowed.origins:#{null}}")
    private var allowedOrigins: String? = null
    override fun afterPropertiesSet() {
        if (allowedOrigins == null) {
            throw IllegalArgumentException("\${allowed.origins} must be configured")
        }
    }
}
