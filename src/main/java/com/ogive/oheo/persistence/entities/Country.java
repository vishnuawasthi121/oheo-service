package com.ogive.oheo.persistence.entities;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

@NamedQuery(name="Country.dropDown", query="SELECT countryCode ,countryName FROM Country WHERE status = 'ACTIVE'")
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

	@Enumerated(EnumType.STRING)
	private StatusCode status;

	@JsonIgnore
	@OneToMany(mappedBy = "country")
	private Set<State> states;

	@JsonIgnore
	@OneToMany(mappedBy = "country")
	private Set<ZoneDetail> zoneDetails;

	
}
