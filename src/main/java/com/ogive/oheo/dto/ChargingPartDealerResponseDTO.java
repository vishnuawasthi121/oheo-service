package com.ogive.oheo.dto;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.springframework.hateoas.RepresentationModel;

import com.ogive.oheo.constants.StatusCode;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChargingPartDealerResponseDTO extends RepresentationModel<ChargingPartDealerResponseDTO> {

	private Long id;

	private String name;

	private String email;

	private String address;

	private String phone;

	private String contactPerson;

	private String gstNumber;

	@Enumerated(EnumType.STRING)
	private StatusCode status;

}
