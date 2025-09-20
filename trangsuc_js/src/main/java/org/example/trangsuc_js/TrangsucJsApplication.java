package org.example.trangsuc_js;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@SpringBootApplication
public class TrangsucJsApplication implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(@NonNull CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOrigins("*")
				.allowedMethods("*")
				.allowedHeaders("*")
				.exposedHeaders("*")
				.allowCredentials(false)
				.maxAge(3600); // Max age of the CORS pre-flight request
	}

    public static void main(String[] args) {
        SpringApplication.run(TrangsucJsApplication.class, args);
    }
}
