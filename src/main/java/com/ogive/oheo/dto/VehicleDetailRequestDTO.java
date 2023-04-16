package com.ogive.oheo.dto;

import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import com.ogive.oheo.constants.StatusCode;
import com.ogive.oheo.persistence.entities.AddressRequestDTO;

public class VehicleDetailRequestDTO {

	private String vehicleName;

	private String keyFeatures;

	private Double price;

	private StatusCode status;

	private Long vehicleTypeId;

	private Long vehicleBodyTypeId;

	private AddressRequestDTO address;

	private Set<MultipartFile> files;

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

	public Long getVehicleTypeId() {
		return vehicleTypeId;
	}

	public void setVehicleTypeId(Long vehicleTypeId) {
		this.vehicleTypeId = vehicleTypeId;
	}

	public Long getVehicleBodyTypeId() {
		return vehicleBodyTypeId;
	}

	public void setVehicleBodyTypeId(Long vehicleBodyTypeId) {
		this.vehicleBodyTypeId = vehicleBodyTypeId;
	}

	public AddressRequestDTO getAddress() {
		return address;
	}

	public void setAddress(AddressRequestDTO address) {
		this.address = address;
	}

	public Set<MultipartFile> getFiles() {
		return files;
	}

	public void setFiles(Set<MultipartFile> files) {
		this.files = files;
	}

	@Override
	public String toString() {
		return "VehicleDetailRequestDTO [vehicleName=" + vehicleName + ", keyFeatures=" + keyFeatures + ", price="
				+ price + ", status=" + status + ", vehicleTypeId=" + vehicleTypeId + ", vehicleBodyTypeId="
				+ vehicleBodyTypeId + ", address=" + address + ", files=" + files + "]";
	}

}
