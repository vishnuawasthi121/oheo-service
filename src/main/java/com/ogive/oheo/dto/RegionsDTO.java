package com.ogive.oheo.dto;

import javax.validation.constraints.NotNull;

public class RegionsDTO {

	@NotNull(message = "name is mandatory")
	private String name;

	private Long id;

	@NotNull(message = "status is mandatory")
	private String status;
}
