package com.ogive.oheo.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.ogive.oheo.constants.StatusCode;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChargingStationRequestDTO {

	@NotEmpty
	private String name;

	@NotEmpty
	@Email
	private String email;

	@NotEmpty
	private String address;

	@NotEmpty
	private String gstNumber;

	private StatusCode status;

	@NotNull
	private Long vehicleTypeId;

	@NotNull
	private Long zoneId;

	@NotNull
	private Long stateId;

	@NotNull
	private Long cityId;

	@NotNull
	private Long zipcode;

	// Dealers are user
	@NotNull
	private Long dealerId;

}
