package com.ogive.oheo.dto;

import javax.validation.constraints.NotNull;

public class CountryDTO {

	private Long id;

	@NotNull(message = "countryCode is mandatory")
	private String countryCode;

	@NotNull(message = "countryName is mandatory")
	private String countryName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

}
