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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.ogive.oheo.constants.StatusCode;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NamedQuery(name="Product.dropDownLive", query="SELECT id ,name FROM Product WHERE isLive = :isLive")
@NamedQuery(name="Product.findProductByUserIdAndId", query="FROM Product WHERE userDetail.id  = :userId AND id = :id")

@Setter
@Getter
@ToString
@NoArgsConstructor

@Table(name = "PRODUCT")
@SequenceGenerator(allocationSize = 1, initialValue = 100, name = "SEQ_PRODUCT", sequenceName = "SEQ_PRODUCT")
@Entity
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PRODUCT")
	private Long id;

	private Double price;

	private String name;

	private String noOfSeats;

	private String engine;

	@Column(name = "IS_LIVE")
	private String isLive = "N";

	@Enumerated
	private StatusCode status;

	@OneToMany(mappedBy = "product")
	private Set<Images> images;

	@OneToOne(mappedBy = "product")
	private ProductSpecification productSpecification;

	@OneToMany(mappedBy = "product")
	private Set<Features> features;

	@ManyToOne
	@JoinColumn(name = "VEHICLE_DETAIL_ID")
	private VehicleDetail vehicleDetail;

	@ManyToOne
	@JoinColumn(name = "VEHICLE_MODEL_ID")
	private VehicleType vehicleType;

	@ManyToOne
	@JoinColumn(name = "VEHICLE_TYPE_ID")
	private VehicleModel vehicleModel;

	@ManyToOne
	@JoinColumn(name = "VEHICLE_FUEL_TYPE_ID")
	private VehicleFuelType vehicleFuelType;

	@ManyToOne
	@JoinColumn(name = "VEHICLE_TRANSMISSION_ID")
	private VehicleTransmission vehicleTransmission;
	
	@ManyToOne
	@JoinColumn(name = "USER_DETAIL_ID")
	private UserDetail userDetail;

}
