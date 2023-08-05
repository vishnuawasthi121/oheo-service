package com.ogive.oheo.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChargingSlotBookingRequestDTO {

	@NotEmpty
	private String fullName;

	@NotEmpty
	private String contactNumber;

	@NotEmpty
	private String stateName;

	@NotEmpty
	private String cityName;

	@NotEmpty
	private String vehicleType;

	@NotEmpty
	private String vehicleModel;

	@NotEmpty
	private String stationType;

	@NotEmpty
	private String chargingType;

	@NotNull
	private Long chargingStationId;

	@NotEmpty
	private String dateTime;

}
