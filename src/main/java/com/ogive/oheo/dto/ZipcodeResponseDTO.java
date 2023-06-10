package com.ogive.oheo.dto;

import org.springframework.hateoas.RepresentationModel;

import com.ogive.oheo.constants.StatusCode;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class ZipcodeResponseDTO extends RepresentationModel<ZipcodeResponseDTO> {

	private Long id;

	private Long code;

	private Long cityId;

	private StatusCode status;

	private String cityName;

}
