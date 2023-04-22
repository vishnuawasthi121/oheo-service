package com.ogive.oheo.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.ogive.oheo.constants.StatusCode;

public class WidgetRequestDTO {

	@NotEmpty(message = "name is mandatory")
	private String name;

	private StatusCode status;

	@NotEmpty(message = "name is mandatory")
	private String content;

	@NotEmpty(message = "name is mandatory")
	private String shortCode;

	@NotNull(message = "positionId is mandatory")
	private Long positionId;

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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getShortCode() {
		return shortCode;
	}

	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}

	public Long getPositionId() {
		return positionId;
	}

	public void setPositionId(Long positionId) {
		this.positionId = positionId;
	}

}
