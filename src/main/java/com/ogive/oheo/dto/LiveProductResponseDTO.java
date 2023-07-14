package com.ogive.oheo.dto;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import com.ogive.oheo.constants.StatusCode;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class LiveProductResponseDTO extends RepresentationModel<LiveProductResponseDTO> {

	private Long id;

	private String engine;

	private String isLive;

	private String name;

	private String noOfSeats;

	private Double price;

	@Enumerated(EnumType.STRING)
	private StatusCode status;

	private String availableForLease;

	private String vehicleBodyType;

	private String fuelType;

	private String vehicleModelName;

	private String vehicleType;

	private String vehicleTransmission;

	// UserDetail
	private String dealerName;

	// Specification
	private String chassisNumber;

	private String color;
	private String displacement;

	private String engineType;

	private String frontBrakeType;

	private String rearBrakeType;

	private String fuelSupplySystem;

	private String gearBox;

	private String height;

	private String isTurboCharger;

	private String length;

	private String noOfCylinder;

	private String power;

	private String seatingCapacity;

	private String steeringType;

	private String torque;

	private String tyreType;

	private String valuesConfiguration;

	private String valuesPerCylinder;

	private String wheelBase;
	private String width;

	private Set<String> features = new HashSet<>();

	private Set<Link> images = new HashSet<>();

}
