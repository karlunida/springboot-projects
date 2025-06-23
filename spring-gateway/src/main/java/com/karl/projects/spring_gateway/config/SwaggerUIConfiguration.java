package com.karl.projects.spring_gateway.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springdoc.core.properties.SwaggerUiConfigParameters;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.karl.projects.spring_gateway.entity.ApiRoute;
import com.karl.projects.spring_gateway.service.ApiRouteService;

import jakarta.annotation.PostConstruct;

@Configuration

public class SwaggerUIConfiguration {
	
	@Autowired
	private ApiRouteService apiRouteService;
	
	public static List<ApiRoute> SwaggerConfigRoutes = new ArrayList<>();
	
	@Bean
	@Primary
	@RefreshScope
	public SwaggerUiConfigProperties swaggerUiConfigProperties() {
		SwaggerUiConfigProperties properties = new SwaggerUiConfigProperties();
		properties.setEnabled(true);
		properties.setPath(DefinedConstants.SwaggerConstants.SWAGGERPATH);
		properties.setConfigUrl(DefinedConstants.SwaggerConstants.SWAGGERCONFIGURL);
		
		SwaggerUiConfigParameters.SwaggerUrl apiGatewayService = new SwaggerUiConfigParameters.SwaggerUrl();
		apiGatewayService.setUrl(DefinedConstants.SwaggerConstants.APIDOCSURL);
		apiGatewayService.setName(DefinedConstants.SwaggerConstants.APIGATEWAYNAME);
		apiGatewayService.setDisplayName(DefinedConstants.SwaggerConstants.APIGATEWAYNAME);
		
		List<ApiRoute> routes = SwaggerConfigRoutes;
		Set<SwaggerUiConfigParameters.SwaggerUrl> urls = new HashSet<>();
		urls.add(apiGatewayService);
		routes.stream().forEach( route -> {
			SwaggerUiConfigParameters.SwaggerUrl swaggerUrl = new SwaggerUiConfigParameters.SwaggerUrl();
			String contextStr = route.getContextRoot() != null ? route.getContextRoot() : route.getRouteName();
			swaggerUrl.setUrl("/"+contextStr+DefinedConstants.SwaggerConstants.APIDOCSURL);
			swaggerUrl.setName(route.getRouteName());
			swaggerUrl.setDisplayName(route.getRouteName());
			urls.add(swaggerUrl);
		});
		properties.setUrls(urls);
		return properties;
		
	}

	@Bean
	public SwaggerUiConfigParameters swaggerUiConfigParameters(SwaggerUiConfigProperties swaggerUiConfigProperties) {
		return new SwaggerUiConfigParameters(swaggerUiConfigProperties);
	}
	
	@PostConstruct
	public void initRoutes() {
		SwaggerConfigRoutes = apiRouteService.findAllActive().collectList().block();
	}
}
