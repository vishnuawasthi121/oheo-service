package com.ogive.oheo.dto;

import javax.validation.constraints.NotNull;

import com.ogive.oheo.constants.StatusCode;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor

public class StateRequestDTO {

	private Long zoneId;

	@NotNull(message = "stateName is mandatory")
	private String stateName;

	@NotNull(message = "stateCode is mandatory")
	private String stateCode;

	@NotNull(message = "satus is mandatory")
	private StatusCode satus;

}
