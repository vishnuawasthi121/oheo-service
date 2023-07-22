package com.ogive.oheo.dto;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LeaseDetailRequestDTO {

	@NotEmpty
	private String description;

	@NotNull
	private Float duration;

	private Float downPayment;

	private String kmValidity;

	@Column(name = "MONTHLY_EMI")
	private float monthlyEMI;

	private MultipartFile image;

	private Long leaseId;

}
