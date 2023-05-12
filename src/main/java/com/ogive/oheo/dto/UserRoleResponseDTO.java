package com.ogive.oheo.dto;

import org.springframework.hateoas.RepresentationModel;

import com.ogive.oheo.constants.RoleTypes;
import com.ogive.oheo.constants.StatusCode;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor

public class UserRoleResponseDTO extends RepresentationModel<UserRoleResponseDTO> {

	private RoleTypes role;

	private StatusCode status;

	private Long id;
}
