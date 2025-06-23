package com.karl.projects.spring_gateway.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.karl.projects.spring_gateway.dto.ApiRouteDTO;
import com.karl.projects.spring_gateway.entity.ApiRoute;
import com.karl.projects.spring_gateway.service.ApiRouteService;
import com.karl.projects.spring_gateway.service.RouteRefreshService;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "/gateway")
public class GatewayController {
	
	@Autowired
	private ApiRouteService apiRouteService;
	
	@Autowired
	private RouteRefreshService routeRefreshService;

	@GetMapping
	public Mono<ResponseEntity<List<ApiRoute>>> findApiRoutes(){
		return apiRouteService.findAll().collectList().map(list -> ResponseEntity.status(HttpStatus.OK).body(list));
	}
	
	@GetMapping("/{routeName}")
	public Mono<ResponseEntity<ApiRoute>> findApiRoute(@PathVariable String routeName){
		return apiRouteService.findRoute(routeName).map(route -> ResponseEntity.status(HttpStatus.OK).body(route));
	}
	
	@PostMapping
	public Mono<ResponseEntity<Object>> createRoute(@Valid @RequestBody ApiRouteDTO apiRouteDTO){
		return apiRouteService.createApiRoute(apiRouteDTO)
		        .map(savedRoute -> ResponseEntity.status(HttpStatus.CREATED).build())
		        .onErrorResume( e -> Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage())))
		        ;
	}
	
	@PutMapping
	public Mono<ResponseEntity<Object>> updateRoute(@Valid @RequestBody ApiRouteDTO apiRouteDTO){
		return apiRouteService.updateApiRoute(apiRouteDTO)
		        .map(savedRoute -> ResponseEntity.status(HttpStatus.NO_CONTENT).build())
		        .onErrorResume( e -> Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage())))
		        ;
	}
	
	@GetMapping("/refresh")
	public Mono<ResponseEntity<Void>> refreshRoutes(){
		return Mono.fromRunnable(() -> {
			routeRefreshService.refreshRoutes();
		}).map( t -> ResponseEntity.status(HttpStatus.NO_CONTENT).build() );
	}
	
}
