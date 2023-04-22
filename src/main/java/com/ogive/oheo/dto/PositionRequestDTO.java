package com.ogive.oheo.dto;

import javax.validation.constraints.NotEmpty;

import com.ogive.oheo.constants.StatusCode;

public class PositionRequestDTO {

	@NotEmpty(message = "name is mandatory")
	private String name;

	@NotEmpty(message = "shortCode is mandatory")
	private String shortCode;

	private StatusCode status;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShortCode() {
		return shortCode;
	}

	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}

	public StatusCode getStatus() {
		return status;
	}

	public void setStatus(StatusCode status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "[name=" + name + ", shortCode=" + shortCode + ", status=" + status + "]";
	}

}
