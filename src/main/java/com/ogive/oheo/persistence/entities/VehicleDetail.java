package com.ogive.oheo.persistence.entities;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.ogive.oheo.constants.StatusCode;

@Table(name = "VEHICLE_DETAIL")
@SequenceGenerator(allocationSize = 1, initialValue = 100, name = "SEQ_VEHICLE_DETAIL", sequenceName = "SEQ_VEHICLE_DETAIL")
@Entity
public class VehicleDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_VEHICLE_DETAIL")
	private Long id;

	@Column(nullable = false)
	private String vehicleName;

	// Add Key Feature
	private String keyFeatures;

	@Column(precision = 2, nullable = false)
	private Double price;

	@Enumerated
	private StatusCode status;

	@ManyToOne()
	@JoinColumn(name = "VEHICLE_TYPE_ID", nullable = false)
	// Select Vehicle Category*
	private VehicleType vehicleType;

	@ManyToOne()
	@JoinColumn(name = "VEHICLE_BODY_TYPE_ID", nullable = false)
	// BODY TYPE
	private VehicleBodyType vehicleBodyType;

	@OneToMany(mappedBy = "vehicleDetail",cascade = CascadeType.ALL)
	private Set<Images> images;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "ADDRESS_ID", nullable = false)
	private Address address;

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

	public VehicleType getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(VehicleType vehicleType) {
		this.vehicleType = vehicleType;
	}

	public VehicleBodyType getVehicleBodyType() {
		return vehicleBodyType;
	}

	public void setVehicleBodyType(VehicleBodyType vehicleBodyType) {
		this.vehicleBodyType = vehicleBodyType;
	}

	public Set<Images> getImages() {
		return images;
	}

	public void setImages(Set<Images> images) {
		this.images = images;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

}
