package com.karl.projects.rest_api.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class EmployeeDTO {

	@Nullable
	private Long id;
	@NotNull
	private String name;
	@NotNull
	private String role;
}
