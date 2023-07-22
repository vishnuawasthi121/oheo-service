package com.ogive.oheo.persistence.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

import org.hibernate.annotations.Immutable;

import com.ogive.oheo.constants.StatusCode;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor

@Immutable
@Entity(name = "view_product_lease")
public class ViewProductLeaseDetail {

	@Id
	private Long leaseId;

	@Column(name = "product_id")
	private Long productId;

	@Column(name = "product_name")
	private String productName;

	@Enumerated(EnumType.STRING)
	private StatusCode status;
	
	@Column(name = "description")
	private String description;

	@Column(name = "duration")
	private Float duration;

	@Column(name = "down_payment")
	private Float downPayment;

	@Column(name = "km_validity")
	private String kmValidity;

	@Column(name = "monthly_emi")
	private float monthlyEMI;

	private Date createdDate;
	private Date updatedDate;

	@Column(name = "image_id")
	private Long imageId;

}
