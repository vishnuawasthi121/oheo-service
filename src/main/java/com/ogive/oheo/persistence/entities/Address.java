package com.ogive.oheo.persistence.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Table(name = "ADDRESS")
@SequenceGenerator(allocationSize = 1, initialValue = 100, name = "SEQ_ADDRESS", sequenceName = "SEQ_ADDRESS")
@Entity
public class Address {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ADDRESS")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "STATE_ID")
	private State state;

	@ManyToOne
	@JoinColumn(name = "ZONE_ID")
	private ZoneDetail zone;

	@ManyToOne
	@JoinColumn(name = "CITY_ID")
	private City city;

	@ManyToOne
	@JoinColumn(name = "ZIPCODE_ID")
	private Zipcode zipcode;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public ZoneDetail getZone() {
		return zone;
	}

	public void setZone(ZoneDetail zone) {
		this.zone = zone;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public Zipcode getZipcode() {
		return zipcode;
	}

	public void setZipcode(Zipcode zipcode) {
		this.zipcode = zipcode;
	}

}
