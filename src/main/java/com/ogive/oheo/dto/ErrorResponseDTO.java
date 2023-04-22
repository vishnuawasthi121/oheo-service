package com.ogive.oheo.dto;

import java.util.HashMap;
import java.util.Map;

public class ErrorResponseDTO {

	private String description;

	private Map<String, String> rejectedFields = new HashMap<>();

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

	public Map<String, String> getRejectedFields() {
		return rejectedFields;
	}

	public void setRejectedFields(Map<String, String> rejectedFields) {
		this.rejectedFields = rejectedFields;
	}

}
