package com.karl.projects.spring_gateway.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.karl.projects.spring_gateway.entity.ApiRoute;
import com.karl.projects.spring_gateway.repository.ApiRouteRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@Service
public class ApiRouteService {
	
	@Autowired
	private ApiRouteRepository apiRouteRepository;
	
	public Flux<ApiRoute> findAll(){
		return Mono.fromCallable(() -> apiRouteRepository.findAll()).subscribeOn(Schedulers.boundedElastic()).flatMapMany(Flux::fromIterable);
	}
	
	public Flux<ApiRoute> findAllActive(){
		return Mono.fromCallable(() -> apiRouteRepository.findIsActive()).subscribeOn(Schedulers.boundedElastic()).flatMapMany(Flux::fromIterable);
	}

}
