package com.ogive.oheo.dto;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ChangePasswordRequestDTO {

	@NotEmpty
	private String password;
	
	@NotEmpty
	private String newPassword;
	
	@NotEmpty
	private String email;

}
