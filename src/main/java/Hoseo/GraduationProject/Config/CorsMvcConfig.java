package Hoseo.GraduationProject.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsMvcConfig implements WebMvcConfigurer {
    @Value("${FrontURL}")
    private String frontUrl;

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry){
        corsRegistry.addMapping("/**")
                .allowedOrigins(frontUrl)
                .allowedHeaders("*")
                .allowedMethods("*")
                .allowCredentials(true)
                .exposedHeaders("*");
    }
}
