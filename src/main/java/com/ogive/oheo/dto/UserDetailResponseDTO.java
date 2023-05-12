package com.ogive.oheo.dto;

import java.util.HashSet;
import java.util.Set;

import org.springframework.hateoas.RepresentationModel;

import com.ogive.oheo.constants.RoleTypes;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDetailResponseDTO extends RepresentationModel<UserDetailResponseDTO> {

	private Long id;

	private String name;

	private String email;

	// private String password;

	private String contact;

	private Long rootId;

	private Set<UserDetailResponseDTO> childs = new HashSet<UserDetailResponseDTO>();

	private RoleTypes role;
}
