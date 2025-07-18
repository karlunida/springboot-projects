package com.karl.projects.spring_gateway.config;

import org.modelmapper.ModelMapper;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.karl.projects.spring_gateway.service.CustomRouteLocator;

@Configuration
public class GatewayConfig {
	
	@Bean
	public RouteLocator myRoutes(RouteLocatorBuilder builder) {
		return new CustomRouteLocator(builder);
	}
	
	@Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
