package com.ogive.oheo.persistence.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor

@NamedQuery(name="ProductSpecification.deleteByProductId", query="delete from ProductSpecification where product.id  =:productId")
@Table(name = "PRODUCT_SPECIFICATION")
@SequenceGenerator(allocationSize = 1, initialValue = 100, name = "SEQ_PRODUCT_SPECIFICATION", sequenceName = "SEQ_PRODUCT_SPECIFICATION")
@Entity
public class ProductSpecification {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PRODUCT_SPECIFICATION")
	private Long id;

	private String color;
	private String engineType;
	private String displacement;
	private String power;
	private String torque;
	private String noOfcylinder;
	private String valuesPerCylinder;
	private String valuesConfiguration;
	private String fuelSupplySystem;
	private String isTurboCharger;

	// Dimensions and capacity
	private String length;
	private String width;
	private String height;
	private String wheelBase;

	// Miscellaneous
	private String gearBox;
	private String seatingCapacity;
	private String steeringType;
	private String frontBrakeType;
	private String rearBrakeType;
	private String tyreType;
	private String chassisNumber;
	
	@JsonIgnore
	// @ManyToOne
	// @JoinColumn(name = "PRODUCT_ID")
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "product_id", referencedColumnName = "id",nullable = true)
	private Product product;

}
