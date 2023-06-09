package com.ogive.oheo.persistence.entities;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
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
@NamedQuery(name="Product.deleteByProductId", query="delete from Product where id  =:productId")

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

	@OneToMany(mappedBy = "product",fetch = FetchType.LAZY)
	private Set<Images> images;

	@OneToOne(mappedBy = "product",fetch = FetchType.LAZY)
	private ProductSpecification productSpecification;

	@OneToMany(mappedBy = "product",fetch = FetchType.LAZY)
	private Set<Features> features;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "VEHICLE_DETAIL_ID")
	private VehicleDetail vehicleDetail;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "VEHICLE_MODEL_ID")
	private VehicleType vehicleType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "VEHICLE_TYPE_ID")
	private VehicleModel vehicleModel;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "VEHICLE_FUEL_TYPE_ID")
	private VehicleFuelType vehicleFuelType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "VEHICLE_TRANSMISSION_ID")
	private VehicleTransmission vehicleTransmission;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_DETAIL_ID")
	private UserDetail userDetail;
	
	private String availableForLease;

}
