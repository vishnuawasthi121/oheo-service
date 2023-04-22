package com.ogive.oheo.dto;

import javax.validation.constraints.NotNull;

import com.ogive.oheo.constants.StatusCode;

public class CountryDTO {

	@NotNull(message = "countryCode is mandatory")
	private String countryCode;

	@NotNull(message = "countryName is mandatory")
	private String countryName;

	private StatusCode status;

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

	public StatusCode getStatus() {
		return status;
	}

	public void setStatus(StatusCode status) {
		this.status = status;
	}

}
