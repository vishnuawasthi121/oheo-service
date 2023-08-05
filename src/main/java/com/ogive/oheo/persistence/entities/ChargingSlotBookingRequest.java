package com.ogive.oheo.persistence.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
//@NamedQuery(name="ChargingProduct.findProductByUserIdAndId", query="FROM ChargingProduct WHERE userDetail.id  = :userId AND id = :id")

@Table(name = "CHARGING_SLOT_BOOKING_REQ")
@SequenceGenerator(allocationSize = 1, initialValue = 100, name = "SEQ_CHARGING_SLOT_BOOKING_REQ", sequenceName = "SEQ_CHARGING_SLOT_BOOKING_REQ")
@Entity
public class ChargingSlotBookingRequest {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CHARGING_SLOT_BOOKING_REQ")
	private Long id;

	private String fullName;

	private String contactNumber;

	private String stateName;

	private String cityName;

	private String vehicleType;

	private String vehicleModel;

	private String stationType;

	private String chargingType;

	@ManyToOne()
	@JoinColumn(name = "CHARGING_STATION_ID", nullable = true)
	private ChargingStation chargingStation;

	private String dateTime;
	
	private String isAccepted;

}
