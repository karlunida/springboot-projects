package com.karl.projects.spring_gateway;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.karl.projects.spring_gateway.dto.ApiRouteDTO;
import com.karl.projects.spring_gateway.entity.ApiRoute;
import com.karl.projects.spring_gateway.repository.ApiRouteRepository;
import com.karl.projects.spring_gateway.service.ApiRouteService;
import com.karl.projects.spring_gateway.service.RouteRefreshService;

import reactor.test.StepVerifier;

@SpringBootTest
class SpringGatewayApplicationTests {
	
	@Autowired
	private ApiRouteRepository repository;

	@Autowired
	private ApiRouteService apiRouteService;
	
	@Autowired
	private RouteRefreshService routeRefreshService;
	
	@Test
	void contextLoads() {
	}
	
	@Test
	void findAllActiveTest() {
		List<ApiRoute> routes = repository.findIsActive();
		assertNotNull(routes);
		
	}
	
	@Test
	void findAndValidateTest() {
		ApiRouteDTO route1 = new ApiRouteDTO();
		route1.setRouteName("test");
		StepVerifier.create(apiRouteService.findAndValidateRoute(route1.getRouteName())).expectNextCount(0).verifyComplete();
	}
	
	@Test
	void routeRefreshTest() {
	}

}
