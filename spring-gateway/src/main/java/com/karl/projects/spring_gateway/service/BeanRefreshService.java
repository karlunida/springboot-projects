package com.karl.projects.spring_gateway.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.endpoint.event.RefreshEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.karl.projects.spring_gateway.config.SwaggerUIConfiguration;
import com.karl.projects.spring_gateway.repository.ApiRouteRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BeanRefreshService {
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@Autowired
	private ApiRouteRepository apiRouteRepository;
	
	public void refreshBean() {
		log.info("initiating bean refresh");
		updateSwaggerConfig();
		publisher.publishEvent(new RefreshEvent(this, null, "manual refresh triggered"));
	}
	
	private void updateSwaggerConfig() {
		SwaggerUIConfiguration.SwaggerConfigRoutes = apiRouteRepository.findIsActive();
	}

}
