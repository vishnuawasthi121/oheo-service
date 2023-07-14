package com.ogive.oheo.persistence.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.ogive.oheo.constants.StatusCode;

@NamedQuery(name="VehicleFuelType.dropDown", query="SELECT id , name FROM VehicleFuelType")

@Table(name = "VEHICLE_FUEL_TYPE")
@SequenceGenerator(allocationSize = 1, initialValue = 100, name = "SEQ_VEHICLE_FUEL_TYPE", sequenceName = "SEQ_VEHICLE_FUEL_TYPE")
@Entity
public class VehicleFuelType {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_VEHICLE_FUEL_TYPE")
	private Long id;

	@Column(nullable = false)
	private String name;

	@Enumerated(EnumType.STRING)
	private StatusCode status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

}
