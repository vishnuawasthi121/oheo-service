package com.ogive.oheo.dto;

import com.ogive.oheo.constants.RoleTypes;
import com.ogive.oheo.constants.StatusCode;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserRoleRequestDTO {

	private RoleTypes role;

	private StatusCode status;
}
