package com.ogive.oheo.dto;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.springframework.hateoas.RepresentationModel;

import com.ogive.oheo.constants.StatusCode;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class ProductLeaseDetailResponseDTO extends RepresentationModel<ProductLeaseDetailResponseDTO> {

	private Long leaseId;

	private Long productId;

	private String productName;

	private StatusCode status;

	private String description;

	@Column(name = "duration")
	private Float duration;

	private Float downPayment;

	private String kmValidity;

	private float monthlyEMI;

	private Date createdDate;
	private Date updatedDate;

	private Long imageId;

}
