package com.ms.userServices.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

	@Value("${app.cors.allowed-origin}")
	private String allowedOrigin;

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		
		

		String[] originArray = allowedOrigin.split(",");
		 
		 System.out.println("Injected CORS Origin: " + allowedOrigin);
		 
		// System.out.println("Request Origin: " + request.getHeader("Origin"));

		    registry.addMapping("/**")
		        .allowedOrigins(originArray)
		        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
		        .allowedHeaders("*")
		        .allowCredentials(true);

	}
}
