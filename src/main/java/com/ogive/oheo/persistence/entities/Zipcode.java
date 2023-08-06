package com.ogive.oheo.persistence.entities;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ogive.oheo.constants.StatusCode;

@NamedQuery(name="Zipcode.dropDown", query="SELECT zipcode.code AS code , city.name AS cityName FROM Zipcode zipcode join City city ON zipcode.city.id = city.id WHERE zipcode.status = 'ACTIVE' ")
@Table(name = "ZIPCODE")
@SequenceGenerator(allocationSize = 1, initialValue = 100, name = "SEQ_ZIPCODE", sequenceName = "SEQ_ZIPCODE")
@Entity
public class Zipcode {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ZIPCODE")
	private Long id;

	@Enumerated(EnumType.STRING)
	private StatusCode status;

	@Column(nullable = false, unique = true)
	private Long code;

	@JsonIgnore(value = true)
	@ManyToOne()
	@JoinColumn(name = "CITY_ID")
	private City city;
	
	@OneToMany(mappedBy = "zipcode",cascade = CascadeType.PERSIST,fetch = FetchType.LAZY)
	private Set<UserDetail> userDetails;
	
	@OneToMany(mappedBy = "zipcode",cascade = CascadeType.PERSIST,fetch = FetchType.LAZY)
	private Set<ChargingStation> chargingStations;

	@OneToMany(mappedBy = "zipcode",cascade = CascadeType.PERSIST,fetch = FetchType.LAZY)
	private Set<Address> addresses;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public StatusCode getStatus() {
		return status;
	}

	public void setStatus(StatusCode status) {
		this.status = status;
	}

	public Long getCode() {
		return code;
	}

	public void setCode(Long code) {
		this.code = code;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	@PreRemove
	private void preRemove() {
		if (null != userDetails) {
			userDetails.forEach(request -> request.setZipcode(null));
		}
		if (null != chargingStations) {
			chargingStations.forEach(request -> request.setZipcode(null));
		}
		if (null != addresses) {
			addresses.forEach(request -> request.setZipcode(null));
		}

	}

	
}
