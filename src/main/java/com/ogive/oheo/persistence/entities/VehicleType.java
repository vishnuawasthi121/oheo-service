package com.ogive.oheo.persistence.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.ogive.oheo.constants.StatusCode;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter



@NamedQuery(name = "VehicleType.dropDown", query = "SELECT id , name FROM VehicleType")
@Table(name = "VEHICLE_TYPE")
@SequenceGenerator(allocationSize = 1, initialValue = 100, name = "SEQ_VEHICLE_TYPE", sequenceName = "SEQ_VEHICLE_TYPE")
@Entity
public class VehicleType {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_VEHICLE_TYPE")
	private Long id;

	@Column(nullable = false)
	private String name;

	@Enumerated(EnumType.STRING)
	private StatusCode status;
}
