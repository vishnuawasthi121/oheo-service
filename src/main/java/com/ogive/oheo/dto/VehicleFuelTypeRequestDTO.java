package com.ogive.oheo.dto;

import javax.validation.constraints.NotEmpty;

import com.ogive.oheo.constants.StatusCode;

public class VehicleFuelTypeRequestDTO {

	@NotEmpty(message = "name is mandatory")
	private String name;

	private StatusCode status;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public StatusCode getStatus() {
		return status;
	}

	public void setStatus(StatusCode status) {
		this.status = status;
	}

}
