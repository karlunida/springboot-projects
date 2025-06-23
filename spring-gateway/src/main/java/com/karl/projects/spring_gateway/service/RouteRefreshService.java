package com.karl.projects.spring_gateway.service;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RouteRefreshService implements ApplicationEventPublisherAware{
	

	private ApplicationEventPublisher applicationEventPublisher;

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;
		
	}
	
	public void refreshRoutes() {
		log.info("Refresh routes initiated");
		applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
	}

}
