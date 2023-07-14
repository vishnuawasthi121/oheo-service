package com.ogive.oheo.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.ogive.oheo.constants.StatusCode;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class CityRequestDTO {

	@NotEmpty(message = "name is mandatory")
	private String name;
	
	private StatusCode status;

	@NotNull(message = "stateId is mandatory")
	private Long stateId;

	
}
