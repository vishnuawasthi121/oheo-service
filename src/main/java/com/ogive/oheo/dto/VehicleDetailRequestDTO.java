package com.ogive.oheo.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.ogive.oheo.constants.StatusCode;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor

public class VehicleDetailRequestDTO {

	@NotEmpty
	private String vehicleName;

	@NotNull
	private Double price;

	private StatusCode status;

	@NotNull
	private Long vehicleTypeId;

	@NotNull
	private Long vehicleBodyTypeId;




}
