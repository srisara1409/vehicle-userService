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

		registry.addMapping("/api/v1/admin/**")
		.allowedOrigins(allowedOrigin)
		.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
		.allowedHeaders("*");

		registry.addMapping("/api/v1/register/**")
		.allowedOrigins(allowedOrigin)
		.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
		.allowedHeaders("*");

		registry.addMapping("/api/v1/vehicle/**")
		.allowedOrigins(allowedOrigin)
		.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
		.allowedHeaders("*");
	}
}
