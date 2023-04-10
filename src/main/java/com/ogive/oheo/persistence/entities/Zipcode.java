package com.ogive.oheo.persistence.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ogive.oheo.constants.StatusCode;

@Table(name = "ZIPCODE")
@SequenceGenerator(allocationSize = 1, initialValue = 100, name = "SEQ_ZIPCODE", sequenceName = "SEQ_ZIPCODE")
@Entity
public class Zipcode {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ZIPCODE")
	private Long id;

	@Enumerated
	private StatusCode status;

	@Column(nullable = false, unique = true)
	private Long code;

	@JsonIgnore(value = true)
	@ManyToOne
	@JoinColumn(name = "CITY_ID")
	private City city;

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

}
