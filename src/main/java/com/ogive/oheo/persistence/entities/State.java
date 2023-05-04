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
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.ogive.oheo.constants.StatusCode;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NamedQuery(name = "State.dropDown", query = "SELECT id ,stateName FROM State")

@Setter
@Getter
@NoArgsConstructor
@ToString

@Table(name = "STATE")
@SequenceGenerator(allocationSize = 1, initialValue = 100, name = "SEQ_STATE", sequenceName = "SEQ_STATE")
@Entity
public class State {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STATE")
	@Column(name = "STATE_ID")
	private Long id;

	@Column(name = "STATE_NAME")
	private String stateName;

	@Column(name = "STATE_CODE", nullable = false, unique = true)
	private String stateCode;

	@Enumerated
	private StatusCode status;

	@ManyToOne
	@JoinColumn(name = "COUNTRY_ID", nullable = false)
	private Country country;

	@ManyToOne
	@JoinColumn(name = "REGION_ID")
	private Regions region;

	@OneToMany(mappedBy = "state")
	private Set<City> cities;

	@ManyToOne
	@JoinColumn(name = "ZONE_ID")
	private ZoneDetail zone;

}
