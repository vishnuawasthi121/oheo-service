package com.ogive.oheo.dto;

import java.util.List;

import org.springframework.hateoas.RepresentationModel;

import com.ogive.oheo.constants.StatusCode;

public class VehicleDetailResponseDTO extends RepresentationModel<VehicleDetailResponseDTO> {

	private Long id;

	private String vehicleName;

	// Add Key Feature
	private String keyFeatures;

	private Double price;

	private StatusCode status;

	private List<FileResponseDTO> images;

	private VehicleTypeResponseDTO vehicleType;

	private VehicleBodyTypeResponseDTO bodyType;

	//
	private AddressResponseDTO address;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getVehicleName() {
		return vehicleName;
	}

	public void setVehicleName(String vehicleName) {
		this.vehicleName = vehicleName;
	}

	public String getKeyFeatures() {
		return keyFeatures;
	}

	public void setKeyFeatures(String keyFeatures) {
		this.keyFeatures = keyFeatures;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public StatusCode getStatus() {
		return status;
	}

	public void setStatus(StatusCode status) {
		this.status = status;
	}

	public List<FileResponseDTO> getImages() {
		return images;
	}

	public void setImages(List<FileResponseDTO> images) {
		this.images = images;
	}

	public VehicleTypeResponseDTO getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(VehicleTypeResponseDTO vehicleType) {
		this.vehicleType = vehicleType;
	}

	public VehicleBodyTypeResponseDTO getBodyType() {
		return bodyType;
	}

	public void setBodyType(VehicleBodyTypeResponseDTO bodyType) {
		this.bodyType = bodyType;
	}

	public AddressResponseDTO getAddress() {
		return address;
	}

	public void setAddress(AddressResponseDTO address) {
		this.address = address;
	}

	
}
