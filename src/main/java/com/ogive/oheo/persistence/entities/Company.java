/**
 * 
 */
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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ogive.oheo.constants.StatusCode;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Vishnu Awasthi
 *
 */

@Setter
@Getter

@NamedQuery(name="Company.dropDown", query="SELECT id , companyName FROM Company WHERE status = 'ACTIVE'")

@Table(name = "VEHICLE_COMPANY")
@SequenceGenerator(allocationSize = 1, initialValue = 100, name = "SEQ_VEHICLE_COMPANY", sequenceName = "SEQ_VEHICLE_COMPANY")
@Entity
public class Company {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_VEHICLE_COMPANY")
	private Long id;

	@Column(nullable = false)
	private String companyName;

	@JsonIgnore
	@OneToMany(mappedBy = "company")
	private Set<VehicleModel> vehicleModels;

	@Enumerated(EnumType.STRING)
	private StatusCode status;
}
