package com.ogive.oheo.dto;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import com.ogive.oheo.constants.StatusCode;

public class ZipcodeRequestDTO {

	@Digits(message="zipcode should contain 6 digits.", fraction = 0, integer = 6)
	@NotNull(message = "code is mandatory")
	private Long zipcode;

	@NotNull(message = "code is mandatory")
	private Long cityId;

	private StatusCode status;

	// private String cityName;

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public StatusCode getStatus() {
		return status;
	}

	public void setStatus(StatusCode status) {
		this.status = status;
	}

	public Long getZipcode() {
		return zipcode;
	}

	public void setZipcode(Long zipcode) {
		this.zipcode = zipcode;
	}

}
