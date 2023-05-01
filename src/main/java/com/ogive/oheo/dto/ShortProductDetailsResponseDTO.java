package com.ogive.oheo.dto;

import org.springframework.hateoas.RepresentationModel;

import com.ogive.oheo.constants.StatusCode;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ShortProductDetailsResponseDTO extends RepresentationModel<ShortProductDetailsResponseDTO> {

	private Long id;
	private String name;
	private StatusCode status;
}
