package com.ogive.oheo.dto;

import org.springframework.hateoas.RepresentationModel;

import com.ogive.oheo.constants.StatusCode;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor

public class SliderResponseDTO extends RepresentationModel<SliderResponseDTO> {

	private Long id;

	private Long productId;

	private String productType;

	private String name;

	private String url;

	private String description;

	private StatusCode status;

}
