package bo.edu.usfx.descuentos;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Value("${app.cors-origins}")
    private String corsOrigins;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        if (corsOrigins != null && corsOrigins.contains("*")) {
            throw new IllegalStateException("CORS_ORIGINS no puede contener un comodín (*)");
        }

        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOriginPatterns(corsOrigins.split(","))
                        .allowedMethods("GET", "POST", "OPTIONS")
                        .allowCredentials(false);
            }
        };
    }
}