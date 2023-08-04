package com.ogive.oheo.persistence.entities;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Immutable;

import com.ogive.oheo.constants.StatusCode;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
/**
 * id, engine, is_live, name, no_of_seats, price, status, available_for_lease,
 * vehicle_body_type, vehicle_fuel_type, vehicle_model_name, vehicle_type_name,
 * vehicle_tras_name, userdetails_name
 * 
 */
@Immutable
@Entity(name = "view_live_product")
public class ViewLiveProduct {

	@Id
	private Long id;

	@Column(name = "engine")
	private String engine;

	@Column(name = "is_live")
	private String isLive;

	@Column(name = "name")
	private String name;

	@Column(name = "no_of_seats")
	private String noOfSeats;

	private Double price;

	@Enumerated(EnumType.STRING)
	private StatusCode status;

	@Column(name = "available_for_lease")
	private String availableForLease;

	@Column(name = "vehicle_body_type")
	private String vehicleBodyType;

	@Column(name = "vehicle_fuel_type")
	private String fuelType;

	@Column(name = "vehicle_model_name")
	private String vehicleModelName;

	@Column(name = "vehicle_type_name")
	private String vehicleType;

	@Column(name = "vehicle_tras_name")
	private String vehicleTransmission;

	// UserDetail
	@Column(name = "userdetails_name")
	private String dealerName;

	// Specification
	@Column(name = "chassis_number")
	private String chassisNumber;

	@Column(name = "color")
	private String color;
	@Column(name = "displacement")
	private String displacement;

	@Column(name = "engine_type")
	private String engineType;

	@Column(name = "front_brake_type")
	private String frontBrakeType;

	@Column(name = "rear_brake_type")
	private String rearBrakeType;

	@Column(name = "fuel_supply_system")
	private String fuelSupplySystem;

	@Column(name = "gear_box")
	private String gearBox;

	@Column(name = "height")
	private String height;

	@Column(name = "is_turbo_charger")
	private String isTurboCharger;

	@Column(name = "length")
	private String length;

	@Column(name = "no_ofcylinder")
	private String noOfCylinder;

	@Column(name = "power")
	private String power;

	@Column(name = "seating_capacity")
	private String seatingCapacity;

	@Column(name = "steering_type")
	private String steeringType;

	@Column(name = "torque")
	private String torque;

	@Column(name = "tyre_type")
	private String tyreType;

	@Column(name = "values_configuration")
	private String valuesConfiguration;

	@Column(name = "values_per_cylinder")
	private String valuesPerCylinder;

	@Column(name = "wheel_base")
	private String wheelBase;

	@Column(name = "width")
	private String width;

	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
	private Set<Features> features;

	@OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
	private Set<Images> images;

}
