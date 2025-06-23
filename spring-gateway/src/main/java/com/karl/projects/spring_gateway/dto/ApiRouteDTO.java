package com.karl.projects.spring_gateway.dto;

import java.util.List;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiRouteDTO {
	@NotNull
	private String routeName;
	@NotNull
	private List<String> routePath;
	@Nullable
	private String routeUri;
	@Nullable
	private List<String> methods;
	@Nullable
	private Boolean active;
	@Nullable
	private String contextRoot;

}
