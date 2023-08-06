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

import com.ogive.oheo.constants.StatusCode;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NamedQuery(name = "State.dropDown", query = "SELECT id ,stateName FROM State WHERE status = 'ACTIVE'")

@Setter
@Getter
@NoArgsConstructor
@ToString

@Table(name = "STATE")
@SequenceGenerator(allocationSize = 1, initialValue = 100, name = "SEQ_STATE", sequenceName = "SEQ_STATE")
@Entity
public class State {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STATE")
	@Column(name = "STATE_ID")
	private Long id;

	@Column(name = "STATE_NAME")
	private String stateName;

	@Column(name = "STATE_CODE", nullable = false, unique = true)
	private String stateCode;

	@Enumerated(EnumType.STRING)
	private StatusCode status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COUNTRY_ID", nullable = false)
	private Country country;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ZONE_ID",nullable = true)
	private ZoneDetail zone;

	@OneToMany(mappedBy = "state", fetch = FetchType.LAZY)
	private Set<City> cities;

	@OneToMany(mappedBy = "state", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	private Set<BuyRequest> buyRequests;

	@OneToMany(mappedBy = "state", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	private Set<VehicleMaintenanceRecord> vehicleMaintenanceRecords;

	@OneToMany(mappedBy = "state", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	private Set<UserDetail> userDetails;

	@OneToMany(mappedBy = "state", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	private Set<ChargingStation> chargingStations;

	@OneToMany(mappedBy = "state", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	private Set<ChargingStationBuyRequest> chargingStationBuyRequests;

	@OneToMany(mappedBy = "state", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	private Set<Address> addresses;

	@PreRemove
	private void preRemove() {

		if (null != buyRequests) {
			buyRequests.forEach(request -> request.setState(null));
		}

		if (null != vehicleMaintenanceRecords) {
			vehicleMaintenanceRecords.forEach(request -> request.setState(null));
		}
		if (null != userDetails) {
			userDetails.forEach(request -> request.setState(null));
		}

		if (null != chargingStations) {
			chargingStations.forEach(request -> request.setState(null));
		}

		if (null != chargingStationBuyRequests) {
			chargingStationBuyRequests.forEach(request -> request.setState(null));
		}

		if (null != addresses) {
			addresses.forEach(request -> request.setState(null));
		}

		if (null != cities) {
			cities.forEach(request -> request.setState(null));
		}

	}
}
