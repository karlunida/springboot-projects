package com.karl.projects.spring_gateway.entity;

import java.util.List;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.karl.projects.spring_gateway.mapper.StringListConverter;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
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
public class ApiRoute implements Persistable<String>{
	
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
	@Column(name = "contextRoot")
	private String contextRoot;
	
	@JsonIgnore
	@Transient
	@Nullable
	private Boolean newRoute;

	@Override
	@JsonIgnore
	@Transient
	public String getId() {
		return this.routeName;
	}

	@Override
	@JsonIgnore
	@Transient
	public boolean isNew() {
		return this.newRoute != null ? this.newRoute : false;
	}	

}
