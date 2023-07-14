package com.ogive.oheo.persistence.entities;

import java.util.Date;

import javax.persistence.Column;
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
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor

@Table(name = "VEHICLE_MAINTENANCE_RECORD")
@SequenceGenerator(allocationSize = 1, initialValue = 100, name = "SEQ_VEHICLE_MAINTENANCE_RECORD", sequenceName = "SEQ_VEHICLE_MAINTENANCE_RECORD")
@Entity
public class VehicleMaintenanceRecord {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_VEHICLE_MAINTENANCE_RECORD")
	private Long id;

	private String name;

	@Column(nullable = false)
	private String email;

	private String number;

	@Enumerated(EnumType.STRING)
	private StatusCode status;

	@ManyToOne
	@JoinColumn(name = "COMPANY_ID")
	private Company company;

	@ManyToOne
	@JoinColumn(name = "VEHICLE_MODEL_ID")
	private VehicleModel vehicleModel;

	@ManyToOne
	@JoinColumn(name = "VEHICLE_FUEL_TYPE_ID")
	private VehicleFuelType vehicleFuelType;

	@ManyToOne
	@JoinColumn(name = "VEHICLE_TYPE_ID")
	private VehicleType vehicleType;

	@ManyToOne
	@JoinColumn(name = "CITY_ID")
	private City city;

	private Date registrationDate;

	// maintenance record
	private Date maintenanceExpirationDate;

	private Integer age;

	// Registration Details
	@ManyToOne
	@JoinColumn(name = "STATE_ID")
	private State state;

	private String registrationAuthority;

	private String chassisNumber;

	private String engineNumber;

	// Vehicle Specification

	private String color;

	private Integer numberOfSeats;

	private Integer numberOfCylinder;

	private String blacklistDetail;

	private String naturalCalamity;

	@ManyToOne
	@JoinColumn(name = "IMAGE_ID")
	private Images image;
	
	private String rtoCode;

}
