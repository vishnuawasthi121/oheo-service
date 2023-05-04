package com.ogive.oheo.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor

public class BuyRequestDTO {

	@NotNull
	private Long companyId;
	@NotNull
	private Long modelId;
	@NotNull
	private Long stateId;
	@NotNull
	private Long cityId;

	// Dealer Id
	private Long dealerId;

	private String initial;
	@NotEmpty
	private String name;
	private String addressLine1;
	private String addressLine2;

	private String mailingStateName;
	private String mailingCityName;
	private String zipcode;
	
	@NotEmpty
	private String contactNumber;
	
	@NotEmpty
	private String email;

}
