package com.ogive.oheo.persistence.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.ogive.oheo.constants.StatusCode;
@NamedQuery(name="ZoneDetail.dropDown", query="SELECT id ,name FROM ZoneDetail WHERE status = 'ACTIVE'")
@Table(name = "ZONE")
@SequenceGenerator(allocationSize = 1, initialValue = 100, name = "SEQ_ZONE", sequenceName = "SEQ_ZONE")
@Entity
public class ZoneDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ZONE")
	@Column(name = "ID")
	private Long id;

	@Column(name = "NAME")
	private String name;

	@Enumerated(EnumType.STRING)
	private StatusCode status;

	private String description;

	@ManyToOne
	@JoinColumn(name = "COUNTRY_ID", nullable = false)
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

}
