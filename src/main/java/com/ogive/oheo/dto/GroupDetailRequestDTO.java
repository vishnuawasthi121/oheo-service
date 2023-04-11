package com.ogive.oheo.dto;

import javax.validation.constraints.NotNull;

import com.ogive.oheo.constants.StatusCode;

public class GroupDetailRequestDTO {

	private Long id;

	@NotNull(message = "groupName is mandatory")
	private String groupName;

	private StatusCode status;

	private String description;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public StatusCode getStatus() {
		return status;
	}

	public void setStatus(StatusCode status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
