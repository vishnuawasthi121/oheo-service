package com.ogive.oheo.dto;

import javax.validation.constraints.NotEmpty;

import com.ogive.oheo.constants.ServiceProviderType;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ServiceProviderRegistrationRequestDTO {

	@NotEmpty
	private String name;

	private String address;

	private String gstNumber;

	private String contactNumber;

	@NotNull
	private Integer number;

	private ServiceProviderType type;

	// TODO - Company/Group
	
	private String panNumber;

}
