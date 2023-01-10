package ch.bfh.habits.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableWebMvc
class WebConfiguration : WebMvcConfigurer {
    @Value("\${allowed.origins}")
    private var allowedOrigins: String? = null

    override fun addCorsMappings(registry: CorsRegistry) {
        println("allowedOrigins: $allowedOrigins")
        registry.addMapping("/**")
            .allowedOrigins(*allowedOrigins!!.split(",").toTypedArray())
            .allowedMethods("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH");
    }
}
