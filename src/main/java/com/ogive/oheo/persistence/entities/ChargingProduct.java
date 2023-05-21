package com.ogive.oheo.persistence.entities;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
@NamedQuery(name="ChargingProduct.findProductByUserIdAndId", query="FROM ChargingProduct WHERE userDetail.id  = :userId AND id = :id")

@Table(name = "CHARGING_PRODUCT")
@SequenceGenerator(allocationSize = 1, initialValue = 100, name = "SEQ_CHARGING_PRODUCT", sequenceName = "SEQ_CHARGING_PRODUCT")
@Entity
public class ChargingProduct {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CHARGING_PRODUCT")
	private Long id;

	@Enumerated
	private StatusCode status;

	private String name;
	private String isLive;
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
	
	@OneToMany(mappedBy = "chargingProduct")
	private Set<Images> images;

	@ManyToOne
	@JoinColumn(name = "USER_DETAIL_ID")
	private UserDetail userDetail;
}
