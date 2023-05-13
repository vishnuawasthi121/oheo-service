package com.ogive.oheo.dto;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequestDTO {

	@NotEmpty
	private String email;

	@NotEmpty
	private String password;
}
