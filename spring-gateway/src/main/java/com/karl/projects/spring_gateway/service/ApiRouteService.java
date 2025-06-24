package com.karl.projects.spring_gateway.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.karl.projects.spring_gateway.dto.ApiRouteDTO;
import com.karl.projects.spring_gateway.entity.ApiRoute;
import com.karl.projects.spring_gateway.mapper.ApiRouteMapper;
import com.karl.projects.spring_gateway.repository.ApiRouteRepository;

import jakarta.transaction.Transactional;
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
		return Flux.defer(() -> Flux.fromIterable(apiRouteRepository.findAll())).subscribeOn(Schedulers.boundedElastic());
	}
	
	public Flux<ApiRoute> findAllActive(){
		return Flux.defer(() -> Flux.fromIterable(apiRouteRepository.findIsActive())).subscribeOn(Schedulers.boundedElastic());
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
			return Mono.fromCallable(() -> apiRouteRepository.save(apiRoute));
		}))
		.subscribeOn(Schedulers.boundedElastic())
		.doOnSuccess(t -> {
			routeRefreshService.refreshRoutes();
			beanRefreshService.refreshBean();
		})
		.then();
		

	}
	
	public Mono<?> updateApiRoute(ApiRouteDTO apiRouteDto) {
		return findAndValidateRoute(apiRouteDto.getRouteName())
			.switchIfEmpty(Mono.error(new IllegalStateException("Route does not exist")))
			.flatMap(existingRoute -> {
				apiRouteMapper.toEntity(apiRouteDto, existingRoute);
				return Mono.fromCallable(() -> apiRouteRepository.save(existingRoute));
			})
			.subscribeOn(Schedulers.boundedElastic())
			.doOnSuccess(route -> {
				routeRefreshService.refreshRoutes();
				beanRefreshService.refreshBean();
			})
			.then();
	}

	
	@Transactional
	public Mono<?> deleteApiRoute(String routeName){
		return findAndValidateRoute(routeName)
				.switchIfEmpty(Mono.error(new IllegalStateException("Route does not exist")))
				.flatMap(existingRoute -> Mono.fromRunnable(() -> {
					apiRouteRepository.delete(existingRoute);
				}).subscribeOn(Schedulers.boundedElastic())).doOnSuccess(route -> {
					routeRefreshService.refreshRoutes();
					beanRefreshService.refreshBean();
				}).then();
	}

}
