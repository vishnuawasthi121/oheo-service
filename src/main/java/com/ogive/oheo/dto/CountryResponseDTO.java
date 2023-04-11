package com.ogive.oheo.dto;

import org.springframework.hateoas.RepresentationModel;

import com.ogive.oheo.constants.StatusCode;

public class CountryResponseDTO extends RepresentationModel<CountryResponseDTO> {

	private Long id;

	private String countryCode;

	private String countryName;

	private StatusCode status;

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

	public StatusCode getStatus() {
		return status;
	}

	public void setStatus(StatusCode status) {
		this.status = status;
	}

}
