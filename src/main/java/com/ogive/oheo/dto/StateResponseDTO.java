package com.ogive.oheo.dto;

import org.springframework.hateoas.RepresentationModel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor

public class StateResponseDTO extends RepresentationModel<StateResponseDTO> {

	private Long id;

	private String stateName;

	private String stateCode;

	private String countryCode;

	private String zoneName;
	
	private Long zoneId;

}
