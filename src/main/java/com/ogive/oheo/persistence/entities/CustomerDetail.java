package com.ogive.oheo.persistence.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Table(name = "CUSTOMER")
@SequenceGenerator(allocationSize = 1, initialValue = 100, name = "SEQ_CUSTOMER", sequenceName = "SEQ_CUSTOMER")
@Entity
public class CustomerDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CUSTOMER")
	private Long id;

	private String firstName;

	private String lastName;

	private String password;

	@Column(nullable = false, unique = true)
	private String email;

}
