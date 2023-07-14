package com.ogive.oheo.persistence.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.ogive.oheo.constants.RoleTypes;
import com.ogive.oheo.constants.StatusCode;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor

@NamedQuery(name = "UserRole.dropDown", query = "SELECT id ,role FROM UserRole")

@Table(name = "USER_ROLE")
@SequenceGenerator(allocationSize = 1, initialValue = 100, name = "SEQ_USER_ROLE", sequenceName = "SEQ_USER_ROLE")
@Entity
public class UserRole {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_USER_ROLE")
	@Column(name = "ID")
	private Long id;

	@Enumerated(EnumType.STRING)
	private RoleTypes role;

	@Enumerated(EnumType.STRING)
	private StatusCode status;

}
