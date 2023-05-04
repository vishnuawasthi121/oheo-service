package com.ogive.oheo.dto;

import org.springframework.hateoas.RepresentationModel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class BuyRequestResponseDTO extends RepresentationModel<BuyRequestResponseDTO> {

	private Long id;

	private String companyName;
	private Long companyId;

	private String modelName;
	private Long modelId;

	private String requestedStateName;
	private Long requestedStateId;

	private String requestedCityName;
	private Long requestedCityId;

	// TODO - Dealer Mapping
	// private UserInfo userInfo;

	private String initial;
	private String name;
	private String addressLine1;
	private String addressLine2;

	private String mailingStateName;
	private String mailingCityName;
	private String zipcode;
	private String contactNumber;
	private String email;

}
