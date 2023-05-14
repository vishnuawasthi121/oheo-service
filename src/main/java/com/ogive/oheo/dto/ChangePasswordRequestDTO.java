package com.ogive.oheo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ChangePasswordRequestDTO {

	private String password;
	private String newPassword;
	private String email;

}
