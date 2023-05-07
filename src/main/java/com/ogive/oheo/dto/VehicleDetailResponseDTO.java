package com.ogive.oheo.dto;

import org.springframework.hateoas.RepresentationModel;

import com.ogive.oheo.constants.StatusCode;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class VehicleDetailResponseDTO extends RepresentationModel<VehicleDetailResponseDTO> {

	private Long id;

	private String vehicleName;

	private Double price;

	private StatusCode status;


	private VehicleTypeResponseDTO vehicleType;

	private VehicleBodyTypeResponseDTO bodyType;
	
}
