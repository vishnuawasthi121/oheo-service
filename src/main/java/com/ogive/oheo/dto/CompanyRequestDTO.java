package com.ogive.oheo.dto;

import javax.validation.constraints.NotEmpty;

import com.ogive.oheo.constants.StatusCode;

public class CompanyRequestDTO {

	@NotEmpty(message = "companyName is mandatory")
	private String companyName;

	private StatusCode status;

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public StatusCode getStatus() {
		return status;
	}

	public void setStatus(StatusCode status) {
		this.status = status;
	}

}
