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

import com.ogive.oheo.constants.StatusCode;

import lombok.Getter;
import lombok.Setter;
@NamedQuery(name="ZoneDetail.dropDown", query="SELECT id ,name FROM ZoneDetail WHERE status = 'ACTIVE'")

@Setter
@Getter

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
	
	@OneToMany(mappedBy = "zone",cascade = CascadeType.PERSIST,fetch = FetchType.LAZY)
	private Set<UserDetail> userDetails;
	
	@OneToMany(mappedBy = "zone",cascade = CascadeType.PERSIST,fetch = FetchType.LAZY)
	private Set<ChargingStation> chargingStations;
	
	@OneToMany(mappedBy = "zone",cascade = CascadeType.PERSIST,fetch = FetchType.LAZY)
	private Set<State> states;

	@OneToMany(mappedBy = "zone",cascade = CascadeType.PERSIST,fetch = FetchType.LAZY)
	private Set<Address> addresses;
	
	@PreRemove
	private void preRemove() {
		if (null != userDetails) {
			userDetails.forEach(request -> request.setZone(null));
		}

		if (null != chargingStations) {
			chargingStations.forEach(request -> request.setZone(null));
		}

		if (null != states) {
			states.forEach(request -> request.setZone(null));
		}

		if (null != addresses) {
			addresses.forEach(request -> request.setZone(null));
		}
	}

}
