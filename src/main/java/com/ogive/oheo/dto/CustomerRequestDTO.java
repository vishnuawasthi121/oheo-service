package com.ogive.oheo.dto;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class CustomerRequestDTO {

	@NotEmpty
	private String firstName;

	@NotEmpty
	private String lastName;

	@NotEmpty
	private String password;

	@NotEmpty
	private String email;

}
