package dutkercz.hi_backend.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Value("${credentials.origin-url}")
    private String ORIGIN_URL;
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Libera todos os endpoints
                .allowedOrigins(ORIGIN_URL)
                .allowedMethods("GET", "POST", "PUT","PATCH", "DELETE", "OPTIONS", "HEAD")
                .allowCredentials(true)
                .allowedHeaders("*");
    }
}
