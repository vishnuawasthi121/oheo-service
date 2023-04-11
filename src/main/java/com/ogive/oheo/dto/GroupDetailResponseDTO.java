package com.ogive.oheo.dto;

import org.springframework.hateoas.RepresentationModel;

import com.ogive.oheo.constants.StatusCode;

public class GroupDetailResponseDTO  extends RepresentationModel<GroupDetailResponseDTO> {

	private Long id;

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
