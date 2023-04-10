package com.ogive.oheo.persistence.entities;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.ogive.oheo.constants.StatusCode;

@Table(name = "REGIONS")
@SequenceGenerator(allocationSize = 1, initialValue = 100, name = "SEQ_REGIONS", sequenceName = "SEQ_REGIONS")
@Entity
public class Regions {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_REGIONS")
	@Column(name="REGION_ID")
	private Long id;

	@Column(nullable = false, unique = true)
	private String name;

	@Enumerated
	private StatusCode status;

	@OneToMany(mappedBy = "region")
	private Set<State> states;

	@ManyToOne
	@JoinColumn(name = "COUNTRY_ID")
	private Country country;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public StatusCode getStatus() {
		return status;
	}

	public void setStatus(StatusCode status) {
		this.status = status;
	}

	public Set<State> getStates() {
		return states;
	}

	public void setStates(Set<State> states) {
		this.states = states;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

}
