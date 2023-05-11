package com.ogive.oheo.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ogive.oheo.constants.StatusCode;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class VehicleMaintenanceRecordRequestDTO {

	@NotEmpty
	private String name;

	@NotEmpty
	private String email;

	private String number;

	private StatusCode status;

	//@NotNull
	private Long companyId;

	///@NotNull
	private Long vehicleModelId;

	//@NotNull
	private Long vehicleFuelTypeId;
	//@NotNull
	private Long cityId;

	@NotNull
	private Long vehicleTypeId;

	//@JsonFormat(pattern="yyyy-MM-dd")
	//@NotNull
//	private String registrationDate;

	//@JsonFormat(pattern="yyyy-MM-dd")
	// maintenance record
	//private String maintenanceExpirationDate;

	private Integer age;

	// Registration Details
	private Long stateId;

	private String registrationAuthority;

	private String chassisNumber;

	private String engineNumber;

	// Vehicle Specification

	private String color;

	private Integer numberOfSeats;

	private Integer numberOfCylinder;

	private String blacklistDetail;

	private String naturalCalamity;

	private MultipartFile image;
	
	private String rtoCode;
	

}
