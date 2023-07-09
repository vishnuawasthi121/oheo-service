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
@NamedQuery(name="ChargingPartDealer.dropDown", query="SELECT id ,name FROM ChargingPartDealer")
@Table(name = "CHARGING_PART_DEALER")
@SequenceGenerator(allocationSize = 1, initialValue = 100, name = "SEQ_CHARGING_PART_DEALER", sequenceName = "SEQ_CHARGING_PART_DEALER")
@Entity
public class ChargingPartDealer {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CHARGING_PART_DEALER")
	private Long id;

	private String name;

	@Column(unique = true,nullable = false)
	private String email;

	private String address;

	private String phone;

	private String contactPerson;

	private String gstNumber;

	@Enumerated(EnumType.STRING)
	private StatusCode status;
}
