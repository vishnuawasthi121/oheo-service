package com.ogive.oheo.dto;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import com.ogive.oheo.constants.StatusCode;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateZipcodeRequestDTO {

	@Digits(message="zipcode should contain 6 digits.", fraction = 0, integer = 6)
	@NotNull(message = "code is mandatory")
	private Long code;

	@NotNull(message = "code is mandatory")
	private Long cityId;

	private StatusCode status;

	

}
