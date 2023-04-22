package com.ogive.oheo.dto;

import java.util.List;

import org.springframework.hateoas.RepresentationModel;

import com.ogive.oheo.constants.StatusCode;

public class FrameResponseDTO extends RepresentationModel<FrameResponseDTO> {

	private String name;

	private StatusCode status;

	private String metaContent;

	List<PositionResponseDTO> positions;

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

	public List<PositionResponseDTO> getPositions() {
		return positions;
	}

	public void setPositions(List<PositionResponseDTO> positions) {
		this.positions = positions;
	}

}
