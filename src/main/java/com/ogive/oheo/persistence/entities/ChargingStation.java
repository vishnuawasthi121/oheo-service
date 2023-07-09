package com.ogive.oheo.persistence.entities;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.ogive.oheo.constants.StatusCode;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

@Table(name = "CHARGING_STATION")
@SequenceGenerator(allocationSize = 1, initialValue = 100, name = "SEQ_CHARGING_STATION", sequenceName = "SEQ_CHARGING_STATION")
@Entity
public class ChargingStation {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CHARGING_STATION")
	private Long id;

	private String name;

	private String email;

	private String address;

	private String gstNumber;

	@Enumerated(EnumType.STRING)
	private StatusCode status;

	@ManyToOne
	@JoinColumn(name = "VEHICLE_TYPE_ID", nullable = true)
	private VehicleType vehicleType;

	@ManyToOne
	@JoinColumn(name = "ZONE_ID", nullable = true)
	private ZoneDetail zone;

	@ManyToOne
	@JoinColumn(name = "STATE_ID", nullable = true)
	private State state;

	@ManyToOne
	@JoinColumn(name = "CITY_ID", nullable = true)
	private City city;

	@ManyToOne
	@JoinColumn(name = "ZIPCODE_ID", nullable = true)
	private Zipcode zipcode;

	@ManyToOne
	@JoinColumn(name = "CHARGING_PART_DEALER_ID", nullable = false)
	private ChargingPartDealer chargingPartDealer;

}
