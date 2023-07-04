package com.ogive.oheo.persistence.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.ogive.oheo.constants.ServiceProviderType;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

@Table(name = "SERVICE_PROVIDERS")
@SequenceGenerator(allocationSize = 1, initialValue = 100, name = "SEQ_SERVICE_PROVIDERS", sequenceName = "SEQ_SERVICE_PROVIDERS")
@Entity
public class ServiceProviders {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SERVICE_PROVIDERS")
	@Column(name = "ID")
	private Long id;

	private String name;

	private String address;

	private String gstNumber;

	private String contactNumber;

	private Integer number;

	@Enumerated(EnumType.STRING)
	private ServiceProviderType type;

	// TODO - Company/Group

	private String panNumber;

}
