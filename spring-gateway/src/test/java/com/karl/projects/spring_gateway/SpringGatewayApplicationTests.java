package com.karl.projects.spring_gateway;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.karl.projects.spring_gateway.entity.ApiRoute;
import com.karl.projects.spring_gateway.repository.ApiRouteRepository;

@SpringBootTest
class SpringGatewayApplicationTests {
	
	@Autowired
	private ApiRouteRepository repository;

	@Test
	void contextLoads() {
	}
	
	@Test
	void findAllActive() {
		List<ApiRoute> routes = repository.findIsActive();
		assertNotNull(routes);
		
	}

}
