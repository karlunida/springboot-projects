package com.karl.projects.spring_gateway.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.karl.projects.spring_gateway.dto.ApiRouteDTO;
import com.karl.projects.spring_gateway.entity.ApiRoute;

import jakarta.annotation.PostConstruct;

@Component
public class ApiRouteMapper {
	
	@Autowired
	private ModelMapper modelMapper;
	
	private TypeMap<ApiRouteDTO, ApiRoute> propertyMapper;
	
	
	public ApiRoute toEntity(ApiRouteDTO apiRouteDto) {
		return modelMapper.map(apiRouteDto, ApiRoute.class);
	}
	
	public ApiRouteDTO toDTO(ApiRoute apiRoute) {
		return modelMapper.map(apiRoute, ApiRouteDTO.class);
	}
	
	public void toEntity(ApiRouteDTO apiRouteDTO, ApiRoute apiRoute) {
		modelMapper.map(apiRouteDTO, apiRoute);
	}
	
	@PostConstruct
	private void init() {
		this.propertyMapper = this.modelMapper.createTypeMap(ApiRouteDTO.class, ApiRoute.class);
		propertyMapper.addMappings(mapper -> mapper.skip(ApiRoute::setActive));
	}

}
