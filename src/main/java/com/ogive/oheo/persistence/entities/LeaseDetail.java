package com.ogive.oheo.persistence.entities;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@ToString
@Setter
@Getter

@Table(name = "LEASE_DETAIL")
@SequenceGenerator(allocationSize = 1, initialValue = 100, name = "SEQ_LEASE_DETAIL", sequenceName = "SEQ_LEASE_DETAIL")
@Entity
public class LeaseDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_LEASE_DETAIL")
	private Long id;

	private String description;

	private Float duration;

	private Float downPayment;

	private String kmValidity;

	@Column(name = "MONTHLY_EMI")
	private float monthlyEMI;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "product_id", referencedColumnName = "id", nullable = true)
	private Product product;

	private Date createdDate;
	private Date updatedDate;

}
