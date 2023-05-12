package com.ogive.oheo.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateUserRequestDTO {
	
	@NotEmpty
	private String name;

	@NotEmpty
	private String contact;

	@NotNull
	private Long roleId;
	
	@NotEmpty
	@Email
	private String email;

}
