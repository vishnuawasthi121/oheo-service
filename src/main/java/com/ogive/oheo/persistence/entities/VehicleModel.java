package com.ogive.oheo.persistence.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.ogive.oheo.constants.StatusCode;

@Table(name = "VEHICLE_MODEL")
@SequenceGenerator(allocationSize = 1, initialValue = 100, name = "SEQ_VEHICLE_MODEL", sequenceName = "SEQ_VEHICLE_MODEL")
@Entity
public class VehicleModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_VEHICLE_MODEL")
	private Long id;

	@Column(nullable = false)
	private String modelName;

	@Enumerated
	private StatusCode status;

	@ManyToOne
	@JoinColumn(name = "COMPANY_ID")
	private Company company;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public StatusCode getStatus() {
		return status;
	}

	public void setStatus(StatusCode status) {
		this.status = status;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

}
