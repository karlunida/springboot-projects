package com.karl.projects.spring_gateway.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.karl.projects.spring_gateway.entity.ApiRoute;
import com.karl.projects.spring_gateway.service.ApiRouteService;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
@RequestMapping(path = "/gateway")
public class GatewayController {
	
	@Autowired
	private ApiRouteService apiRouteService;

	@GetMapping
	public Mono<List<ApiRoute>> findApiRoutes(){
		return apiRouteService.findAll().collectList().subscribeOn(Schedulers.boundedElastic());
	}
}
