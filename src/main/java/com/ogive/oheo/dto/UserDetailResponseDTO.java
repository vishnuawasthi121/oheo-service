package com.ogive.oheo.dto;

import org.springframework.hateoas.RepresentationModel;

import com.ogive.oheo.constants.RoleTypes;
import com.ogive.oheo.constants.StatusCode;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDetailResponseDTO extends RepresentationModel<UserDetailResponseDTO> {

	private Long id;
	private String name;
	private String contact;
	private String email;
	private String isValidated;
	//private String password;

	private StatusCode status;
	
	private String zonename;
	
	private String statename;
	
	private String cityname;
	
	private String zipcode;
	
	private RoleTypes rolename;
	private Long roleId;
	private Long rootId;
	private String gstNumber;
	
	private Long vehicleTypeId;
	private String vehicleTypeName;
}
