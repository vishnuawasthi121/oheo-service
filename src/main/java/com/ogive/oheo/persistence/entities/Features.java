package com.ogive.oheo.persistence.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor

@Table(name = "FEATURES")
@SequenceGenerator(allocationSize = 1, initialValue = 100, name = "SEQ_FEATURES", sequenceName = "SEQ_FEATURES")
@Entity
public class Features {

	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_FEATURES")
	private Long id;

	private String name;

	@ManyToOne
	@JoinColumn(name = "PRODUCT_ID")
	private Product product;

}
