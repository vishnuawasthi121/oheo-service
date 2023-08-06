package com.ogive.oheo.persistence.entities;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ogive.oheo.constants.StatusCode;
@NamedQuery(name="City.dropDown", query="SELECT id ,name FROM City WHERE status = 'ACTIVE'")
@NamedQuery(name="City.dropDownByStateId", query="SELECT id ,name FROM City WHERE state.id = : stateId AND status = 'ACTIVE'")
@Table(name = "CITY")
@SequenceGenerator(allocationSize = 1, initialValue = 100, name = "SEQ_CITY", sequenceName = "SEQ_CITY")
@Entity
public class City {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CITY")
	private Long id;

	@Enumerated(EnumType.STRING)
	private StatusCode status;

	@Column(nullable = false)
	private String name;

	@JsonIgnore(value = true)
	@ManyToOne(cascade = CascadeType.PERSIST,fetch = FetchType.LAZY)
	@JoinColumn(name = "STATE_ID")
	private State state;
	
	@OneToMany(mappedBy = "city",cascade = CascadeType.PERSIST,fetch = FetchType.LAZY)
	private Set<Zipcode> zipcodes;
	
	@OneToMany(mappedBy = "city",cascade = CascadeType.PERSIST,fetch = FetchType.LAZY)
	private Set<BuyRequest> buyRequests;
	
	@OneToMany(mappedBy = "city",cascade = CascadeType.PERSIST,fetch = FetchType.LAZY)
	private Set<VehicleMaintenanceRecord> vehicleMaintenanceRecords;
	
	@OneToMany(mappedBy = "city",cascade = CascadeType.PERSIST,fetch = FetchType.LAZY)
	private Set<UserDetail> userDetails;
	
	@OneToMany(mappedBy = "city",cascade = CascadeType.PERSIST,fetch = FetchType.LAZY)
	private Set<ChargingStation> chargingStations;
	
	@OneToMany(mappedBy = "city",cascade = CascadeType.PERSIST,fetch = FetchType.LAZY)
	private Set<ChargingStationBuyRequest> chargingStationBuyRequests;

	@OneToMany(mappedBy = "city",cascade = CascadeType.PERSIST,fetch = FetchType.LAZY)
	private Set<Address> addresses;
	
	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public StatusCode getStatus() {
		return status;
	}

	public void setStatus(StatusCode status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@PreRemove
	private void preRemove() {

		if (null != zipcodes) {
			zipcodes.forEach(zipcode -> zipcode.setCity(null));
		}
		if (null != buyRequests) {
			buyRequests.forEach(request -> request.setCity(null));
		}

		if (null != vehicleMaintenanceRecords) {
			vehicleMaintenanceRecords.forEach(request -> request.setCity(null));
		}
		if (null != userDetails) {
			userDetails.forEach(request -> request.setCity(null));
		}

		if (null != chargingStations) {
			chargingStations.forEach(request -> request.setCity(null));
		}

		if (null != chargingStationBuyRequests) {
			chargingStationBuyRequests.forEach(request -> request.setCity(null));
		}
		
		if (null != addresses) {
			addresses.forEach(request -> request.setCity(null));
		}
	}

}
