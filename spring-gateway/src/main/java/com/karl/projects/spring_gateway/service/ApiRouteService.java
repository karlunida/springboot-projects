package com.karl.projects.spring_gateway.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.karl.projects.spring_gateway.dto.ApiRouteDTO;
import com.karl.projects.spring_gateway.entity.ApiRoute;
import com.karl.projects.spring_gateway.mapper.ApiRouteMapper;
import com.karl.projects.spring_gateway.repository.ApiRouteRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@Slf4j
public class ApiRouteService {
	
	@Autowired
	private ApiRouteRepository apiRouteRepository;
	
	@Autowired
	private ApiRouteMapper apiRouteMapper;
	
	@Autowired
	private RouteRefreshService routeRefreshService;
	
	@Autowired
	private BeanRefreshService beanRefreshService;
	
	public Flux<ApiRoute> findAll(){
		return Mono.fromCallable(() -> apiRouteRepository.findAll()).subscribeOn(Schedulers.boundedElastic()).flatMapMany(Flux::fromIterable);
	}
	
	public Flux<ApiRoute> findAllActive(){
		return Mono.fromCallable(() -> apiRouteRepository.findIsActive()).subscribeOn(Schedulers.boundedElastic()).flatMapMany(Flux::fromIterable);
	}
	
	public Mono<ApiRoute> findRoute(String routeName){
		return Mono.fromCallable(() -> apiRouteRepository.findById(routeName).orElseThrow()).subscribeOn(Schedulers.boundedElastic());
	}
	
	public Mono<ApiRoute> findAndValidateRoute(String routeName) {
		return Mono.defer(() -> apiRouteRepository.findById(routeName).map(Mono::just).orElseGet(Mono::empty)).subscribeOn(Schedulers.boundedElastic());
	}
	
	public Mono<?> createApiRoute(ApiRouteDTO apiRouteDto) {
		return findAndValidateRoute(apiRouteDto.getRouteName()).flatMap(existingRoute -> {
			return Mono.error(new IllegalStateException("Route already exists"));
		}).switchIfEmpty(Mono.defer(() -> {
			ApiRoute apiRoute = apiRouteMapper.toEntity(apiRouteDto);
			apiRoute.setNewRoute(true);
			apiRoute.setActive(true);
			return Mono.just(apiRouteRepository.save(apiRoute));
		}))
		.doOnSuccess(t -> {
			routeRefreshService.refreshRoutes();
			beanRefreshService.refreshBean();
		})
		.subscribeOn(Schedulers.boundedElastic());

	}
	
	public Mono<?> updateApiRoute(ApiRouteDTO apiRouteDto){
		return findAndValidateRoute(apiRouteDto.getRouteName()).flatMap(existingRoute -> {
			existingRoute = apiRouteMapper.toEntity(apiRouteDto);
			return Mono.just(apiRouteRepository.save(existingRoute));
		}).switchIfEmpty(Mono.error(new IllegalStateException("Route does not exists")))
		.doOnSuccess(t -> {
			routeRefreshService.refreshRoutes();
			beanRefreshService.refreshBean();
		})
		.subscribeOn(Schedulers.boundedElastic());
	}

}
