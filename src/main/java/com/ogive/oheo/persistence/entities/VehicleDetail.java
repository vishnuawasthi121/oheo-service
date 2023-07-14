package com.ogive.oheo.persistence.entities;

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

@Table(name = "VEHICLE_DETAIL")
@SequenceGenerator(allocationSize = 1, initialValue = 100, name = "SEQ_VEHICLE_DETAIL", sequenceName = "SEQ_VEHICLE_DETAIL")
@Entity
public class VehicleDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_VEHICLE_DETAIL")
	private Long id;

	@Column(nullable = false)
	private String vehicleName;

	@Column(precision = 2, nullable = false)
	private Double price;

	@Enumerated(EnumType.STRING)
	private StatusCode status;

	@ManyToOne()
	@JoinColumn(name = "VEHICLE_TYPE_ID", nullable = false)
	private VehicleType vehicleType;
	
	@ManyToOne()
	@JoinColumn(name = "VEHICLE_BODY_TYPE_ID", nullable = false)
	private VehicleBodyType vehicleBodyType;
	
}
