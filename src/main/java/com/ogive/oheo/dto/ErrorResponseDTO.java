package com.ogive.oheo.dto;

public class ErrorResponseDTO {

	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ErrorResponseDTO() {
	}

	public ErrorResponseDTO(String description) {
		super();
		this.description = description;
	}

}
