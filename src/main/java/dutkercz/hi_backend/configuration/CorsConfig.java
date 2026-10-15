package dutkercz.hi_backend.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Libera todos os endpoints
                .allowedOrigins("https://hi-react-cbzpcusmm-i-krriz-team.vercel.app/")
                .allowedMethods("GET", "POST", "PUT","PATCH", "DELETE", "OPTIONS", "HEAD")
                .allowCredentials(true)
                .allowedHeaders("*");
    }
}
