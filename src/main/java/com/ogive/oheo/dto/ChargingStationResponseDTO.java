package com.ogive.oheo.dto;

import org.springframework.hateoas.RepresentationModel;

import com.ogive.oheo.constants.StatusCode;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChargingStationResponseDTO extends RepresentationModel<ChargingStationResponseDTO> {

	private Long id;
	
	private String name;

	private String email;

	private String address;

	private String gstNumber;

	private StatusCode status;

	private String vehicleType;

	private String zoneName;

	private String stateName;

	private String cityName;

	private Long zipcode;

	// Dealers are uses
	private String dealerName;
}
