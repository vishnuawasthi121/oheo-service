/**
 * 
 */
package com.ogive.oheo.persistence.entities;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ogive.oheo.constants.StatusCode;

/**
 * @author Vishnu Awasthi
 *
 */
@NamedQuery(name="Company.dropDown", query="SELECT id , companyName FROM Company")

@Table(name = "VEHICLE_COMPANY")
@SequenceGenerator(allocationSize = 1, initialValue = 100, name = "SEQ_VEHICLE_COMPANY", sequenceName = "SEQ_VEHICLE_COMPANY")
@Entity
public class Company {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_VEHICLE_COMPANY")
	private Long id;

	@Column(nullable = false)
	private String companyName;

	@JsonIgnore
	@OneToMany(mappedBy = "company")
	private Set<VehicleModel> vehicleModels;

	@Enumerated
	private StatusCode status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public StatusCode getStatus() {
		return status;
	}

	public void setStatus(StatusCode status) {
		this.status = status;
	}

	public Set<VehicleModel> getVehicleModels() {
		return vehicleModels;
	}

	public void setVehicleModels(Set<VehicleModel> vehicleModels) {
		this.vehicleModels = vehicleModels;
	}

}
