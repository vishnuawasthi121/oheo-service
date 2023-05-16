package com.ogive.oheo.dto;

import org.springframework.hateoas.RepresentationModel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor

public class CustomerResponseDTO extends RepresentationModel<CustomerResponseDTO> {

	private Long id;
	private String firstName;

	private String lastName;

	//private String password;

	private String email;
}
