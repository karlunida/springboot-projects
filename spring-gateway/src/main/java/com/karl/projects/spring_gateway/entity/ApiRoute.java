package com.karl.projects.spring_gateway.entity;

import java.util.List;

import com.karl.projects.spring_gateway.config.StringListConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ApiRoute", schema = "dbo")
public class ApiRoute {
	
	@Id
	@Column(name = "routeName")
	private String routeName;
	@Column(name = "routePath")
	@Convert(converter = StringListConverter.class)
	private List<String> routePath;
	@Column(name = "routeUri")
	private String routeUri;
	@Column(name = "methods")
	@Convert(converter = StringListConverter.class)
	private List<String> methods;
	@Column(name = "active")
	private Boolean active;
	

}
