package com.ogive.oheo.dto;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import com.ogive.oheo.constants.StatusCode;

public class FrameRequestDTO {

	@NotEmpty(message = "name is mandatory")
	private String name;

	private StatusCode status;

	@NotEmpty(message = "name is metaContent")
	private String metaContent;

	List<Long> positions;

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

	public String getMetaContent() {
		return metaContent;
	}

	public void setMetaContent(String metaContent) {
		this.metaContent = metaContent;
	}

	public List<Long> getPositions() {
		return positions;
	}

	public void setPositions(List<Long> positions) {
		this.positions = positions;
	}

}
