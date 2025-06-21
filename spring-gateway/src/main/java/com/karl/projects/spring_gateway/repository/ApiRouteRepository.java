package com.karl.projects.spring_gateway.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.karl.projects.spring_gateway.entity.ApiRoute;

public interface ApiRouteRepository extends JpaRepository<ApiRoute, String>{

	@Query("Select a from ApiRoute a where a.active = true")
	public List<ApiRoute> findIsActive();
}
