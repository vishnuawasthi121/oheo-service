package com.ogive.oheo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor

public class UserDetailRequestDTO {

	private String name;

	private String email;

	private String password;

	private String contact;

	private Long roleId;

	private String createdByUser;
}
