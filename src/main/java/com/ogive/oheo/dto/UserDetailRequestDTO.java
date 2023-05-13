package com.ogive.oheo.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.ogive.oheo.constants.StatusCode;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor

public class UserDetailRequestDTO {

	@NotEmpty
	private String name;

	@NotEmpty
	@Email
	private String email;

	@NotEmpty
	private String password;

	@NotEmpty
	private String contact;

	private StatusCode status;

	@NotNull
	private Long roleId;

	@NotEmpty
	@Email
	private String createdByUser;

	@NotNull
	private Long zoneId;

	@NotNull
	private Long stateId;

	@NotNull
	private Long cityId;

	@NotNull
	private Long zipcodeId;

}
