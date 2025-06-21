package com.karl.projects.spring_gateway.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.BooleanSpec;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;

import com.karl.projects.spring_gateway.config.DefinedConstants;
import com.karl.projects.spring_gateway.entity.ApiRoute;

import reactor.core.publisher.Flux;

public class CustomRouteLocator implements RouteLocator{
	
	@Autowired
	private ApiRouteService apiRouteService;
	private RouteLocatorBuilder builder;
	
	public CustomRouteLocator(RouteLocatorBuilder builder) {
		super();
		this.builder = builder;
	}

	@Override
	public Flux<Route> getRoutes() {
		RouteLocatorBuilder.Builder routeBuilder = builder.routes();
		return apiRouteService.findAllActive().map(
				apiRoute -> routeBuilder.route(apiRoute.getRouteName(), predicateSpec -> setPredicateSpec(apiRoute, predicateSpec))
				).collectList().flatMapMany(builder -> routeBuilder.build().getRoutes());
	}
	
	private Buildable<Route> setPredicateSpec(ApiRoute apiRoute, PredicateSpec predicateSpec){
		List<String> routePaths = apiRoute.getRoutePath();
		routePaths.add(getSwaggerUrl(apiRoute));
		BooleanSpec spec = predicateSpec.path(routePaths.toArray(new String[0]));
		if(apiRoute.getMethods() != null && !apiRoute.getMethods().isEmpty()) {
			spec.and().method(apiRoute.getMethods().toArray(new String[0]));
		}
				
		//TODO add filter
		return spec.uri(apiRoute.getRouteUri());
	}
	
	// add extra path for accessing openapi page
	private String getSwaggerUrl(ApiRoute apiRoute) {
		String contextRoot = apiRoute.getContextRoot() != null ? apiRoute.getContextRoot() : apiRoute.getRouteName();
		return "/"+contextRoot+DefinedConstants.SwaggerConstants.APIDOCSURL;
	}

}
