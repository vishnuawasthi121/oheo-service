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
@NoArgsConstructor
@ToString

@Table(name = "BUY_REQUEST")
@SequenceGenerator(allocationSize = 1, initialValue = 100, name = "SEQ_BUY_REQUEST", sequenceName = "SEQ_BUY_REQUEST")
@Entity
public class BuyRequest {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_BUY_REQUEST")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "COMPANY_ID")
	private Company company;

	@ManyToOne
	@JoinColumn(name = "MODEL_ID")
	private VehicleModel model;

	@ManyToOne
	@JoinColumn(name = "STATE_ID")
	private State state;

	@ManyToOne
	@JoinColumn(name = "CITY_ID")
	private City city;

	/*
	 * @ManyToOne
	 * 
	 * @JoinColumn(name = "USER_INFO_ID") private UserInfo userInfo;
	 */

	private String initial;
	private String name;
	private String addressLine1;
	private String addressLine2;

	private String mailingStateName;
	private String mailingCityName;
	
	private String zipcode;
	private String contactNumber;
	private String email;

}
