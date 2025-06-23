package com.karl.projects.spring_gateway.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.karl.projects.spring_gateway.dto.ApiRouteDTO;
import com.karl.projects.spring_gateway.entity.ApiRoute;

@Component
public class ApiRouteMapper {
	@Autowired
	private ModelMapper modelMapper;
	
	public ApiRoute toEntity(ApiRouteDTO apiRouteDto) {
		return modelMapper.map(apiRouteDto, ApiRoute.class);
	}
	
	public ApiRouteDTO toDTO(ApiRoute apiRoute) {
		return modelMapper.map(apiRoute, ApiRouteDTO.class);
	}
	

}
