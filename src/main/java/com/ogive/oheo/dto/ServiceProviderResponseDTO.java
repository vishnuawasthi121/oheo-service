package com.ogive.oheo.dto;

import org.springframework.hateoas.RepresentationModel;

import com.ogive.oheo.constants.ServiceProviderType;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ServiceProviderResponseDTO extends RepresentationModel<ServiceProviderResponseDTO> {

	private Long id;

	private String name;

	private String address;

	private String gstNumber;

	private String contactNumber;

	private Integer number;

	private ServiceProviderType type;

	// TODO - Company/Group

	private String panNumber;
}
