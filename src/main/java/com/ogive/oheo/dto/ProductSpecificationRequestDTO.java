package com.ogive.oheo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class ProductSpecificationRequestDTO {
	
	private String color;
	private String engineType;
	private String displacement;
	private String power;
	private String torque;
	private String noOfcylinder;
	private String valuesPerCylinder;
	private String valuesConfiguration;
	private String fuelSupplySystem;
	private String isTurboCharger;

	// Dimensions and capacity
	private String length;
	private String width;
	private String height;
	private String wheelBase;

	// Miscellaneous
	private String gearBox;
	private String seatingCapacity;
	private String steeringType;
	private String frontBrakeType;
	private String rearBrakeType;
	private String tyreType;
	private String chassisNumber;
	
}
