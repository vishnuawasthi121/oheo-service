package com.ogive.oheo.persistence.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;

import org.hibernate.annotations.Immutable;

import com.ogive.oheo.constants.RoleTypes;
import com.ogive.oheo.constants.StatusCode;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor

@Immutable
@Entity
//@Table(name = "view_user_details")
public class ViewUserDetails {

	@Id
	private Long id;

	private String name;
	private String contact;
	private String email;

	@Column(name = "is_validated")
	private String isValidated;
	private String password;

	@Enumerated
	private StatusCode status;
	
	private String zonename;
	private String statename;
	private String cityname;
	@Column(name = "code")
	private String zipcode;
	
	@Enumerated
	private RoleTypes rolename;
	
	@Column(name="roleid")
	private Long roleId;
	
	@Column(name="root_id")
	private Long rootId;
}
