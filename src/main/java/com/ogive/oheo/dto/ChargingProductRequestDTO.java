package com.ogive.oheo.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import com.ogive.oheo.constants.ImageType;
import com.ogive.oheo.constants.StatusCode;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor

public class ChargingProductRequestDTO {

	private StatusCode status;

	@NotEmpty
	private String name;

	@NotNull
	private Double price;

	private String ratedPower;

	private String inputVolt;

	private String numberOfOutput;

	private String outputCurrentRange;

	private String outputChargingOutlet;
	private String statusIndicator;

	private String billing;

	private String chargingOperation;

	private String mechanicalProtection;

	private String certificate;

	private String mounting;

	// Product Specification
	private String asPer;
	private String weight;
	private String dimentions;
	private String optionalAccessories;

	// Protection
	private String inOutProtection;
	private String cooling;

	// Environment
	private String ambientTemperature;
	private String storageTemperature;
	private String operationalTemperature;
	private String altitude;
	private String humidity;
	// Communication
	private String externalCommunication;

	// User interface and Control function
	private String display;
	private String pushButton;
	private String userAuthentication;

	// Input Power
	private String outputVoltage;

	private MultipartFile image;
	private ImageType imageType;
	private String isLive = "N";

}
