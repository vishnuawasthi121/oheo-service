package com.ogive.oheo.dto;

import com.ogive.oheo.constants.StatusCode;

public class ZoneDetailRequestDTO {

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
