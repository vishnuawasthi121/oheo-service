package com.ogive.oheo.persistence.entities;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ogive.oheo.constants.StatusCode;


@NamedQuery(name="Country.dropDown", query="SELECT countryCode ,countryName FROM Country")
@Table(name = "COUNTRY")
@SequenceGenerator(allocationSize = 1, initialValue = 100, name = "SEQ_COUNTRY", sequenceName = "SEQ_COUNTRY")
@Entity
public class Country {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_COUNTRY")
	@Column(name = "COUNTRY_ID")
	private Long id;

	@NotBlank(message = "countryCode is mandatory")
	@Column(name = "COUNTRY_CODE", nullable = false, unique = true)
	private String countryCode;

	@NotBlank(message = "countryName is mandatory")
	@Column(name = "COUNTRY_NAME")
	private String countryName;

	private StatusCode status;

	@JsonIgnore
	@OneToMany(mappedBy = "country")
	private Set<State> states;

	@JsonIgnore
	@OneToMany(mappedBy = "country")
	private Set<Regions> regions;

	@JsonIgnore
	@OneToMany(mappedBy = "country")
	private Set<ZoneDetail> zoneDetails;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public Set<State> getStates() {
		return states;
	}

	public void setStates(Set<State> states) {
		this.states = states;
	}

	public Set<Regions> getRegions() {
		return regions;
	}

	public void setRegions(Set<Regions> regions) {
		this.regions = regions;
	}

	public Set<ZoneDetail> getZoneDetails() {
		return zoneDetails;
	}

	public void setZoneDetails(Set<ZoneDetail> zoneDetails) {
		this.zoneDetails = zoneDetails;
	}

	public StatusCode getStatus() {
		return status;
	}

	public void setStatus(StatusCode status) {
		this.status = status;
	}

}
