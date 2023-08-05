package com.ogive.oheo.dto;

import org.springframework.hateoas.RepresentationModel;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChargingSlotBookingResponseDTO extends RepresentationModel<ChargingSlotBookingResponseDTO> {

	private Long id;
	
	private String fullName;

	private String contactNumber;

	private String stateName;

	private String cityName;

	private String vehicleType;

	private String vehicleModel;

	private String stationType;

	private String chargingType;

	private String chargingStation;

	private String dateTime;
	
	
}
