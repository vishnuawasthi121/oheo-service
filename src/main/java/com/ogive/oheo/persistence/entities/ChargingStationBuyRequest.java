package com.ogive.oheo.persistence.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

@Table(name = "CHARGING_STATION_BUY_REQUEST")
@SequenceGenerator(allocationSize = 1, initialValue = 100, name = "SEQ_CHARGING_STATION_BUY_REQUEST", sequenceName = "SEQ_CHARGING_STATION_BUY_REQUEST")
@Entity
public class ChargingStationBuyRequest {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CHARGING_STATION_BUY_REQUEST")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "COMPANY_ID")
	private Company company;

	@ManyToOne
	@JoinColumn(name = "MODEL_ID")
	private VehicleModel model;

	@ManyToOne
	@JoinColumn(name = "STATE_ID")
	private State state;

	@ManyToOne
	@JoinColumn(name = "CITY_ID")
	private City city;
	
	@ManyToOne
	@JoinColumn(name = "CHARGING_PRODUCT_ID",nullable = true)
	private ChargingProduct chargingProduct;

	@ManyToOne
	@JoinColumn(name = "CHARGING_PART_DEALER_ID", nullable = true)
	private ChargingPartDealer chargingPartDealer;

	private String initial;
	private String name;
	private String addressLine1;
	private String addressLine2;

	private String mailingStateName;
	private String mailingCityName;

	private String zipcode;
	private String contactNumber;
	private String email;
}
