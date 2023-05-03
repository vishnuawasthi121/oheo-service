package com.ogive.oheo.dto;

import java.util.Date;

import org.springframework.hateoas.RepresentationModel;

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
public class VehicleMaintenanceRecordResponseDTO extends RepresentationModel<VehicleMaintenanceRecordResponseDTO> {

	private Long id;

	private String name;

	private String email;

	private String number;

	private StatusCode status;

	private Long companyId;
	private String companyName;

	private Long modelId;
	private String modelName;

	private Long vehicleFuelTypeId;
	private String vehicleFuelTypeName;

	private Long vehicleTypeId;
	private String vehicleTypeName;

	private Long cityId;
	private String cityName;

	@JsonFormat(pattern="yyyy-MM-dd")
	private Date registrationDate;

	// maintenance record
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date maintenanceExpirationDate;

	private Integer age;

	private Long stateId;
	private String stateName;
	private String stateCode;

	private String registrationAuthority;

	private String chassisNumber;

	private String engineNumber;

	// Vehicle Specification

	private String color;

	private Integer numberOfSeats;

	private Integer numberOfCylinder;

	private String blacklistDetail;

	private String naturalCalamity;

}
