package com.ogive.oheo.dto;

import org.springframework.hateoas.RepresentationModel;

import com.ogive.oheo.constants.StatusCode;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class VehicleTypeResponseDTO extends RepresentationModel<VehicleTypeResponseDTO> {

	private String name;

	private Long id;

	private StatusCode status;

	private String description;

}
