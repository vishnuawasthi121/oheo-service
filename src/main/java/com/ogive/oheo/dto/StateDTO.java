package com.ogive.oheo.dto;

import javax.validation.constraints.NotNull;

import com.ogive.oheo.constants.StatusCode;

public class StateDTO {

	private Long id;

	@NotNull(message = "stateName is mandatory")
	private String stateName;

	@NotNull(message = "stateCode is mandatory")
	private String stateCode;

	@NotNull(message = "satus is mandatory")
	private StatusCode satus;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public StatusCode getSatus() {
		return satus;
	}

	public void setSatus(StatusCode satus) {
		this.satus = satus;
	}

}
