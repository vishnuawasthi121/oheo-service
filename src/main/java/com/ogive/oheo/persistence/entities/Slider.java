package com.ogive.oheo.persistence.entities;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.ogive.oheo.constants.ImageType;
import com.ogive.oheo.constants.StatusCode;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor

@NamedQuery(name="Slider.deleteByProductId", query="delete from Slider where product.id  =:productId")
@Table(name = "SLIDER")
@SequenceGenerator(allocationSize = 1, initialValue = 100, name = "SEQ_SLIDER", sequenceName = "SEQ_SLIDER")
@Entity

public class Slider {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SLIDER")
	private Long id;

	private String name;

	@Lob
	private byte[] data;

	@Enumerated
	private ImageType imageType;

	private String description;

	private StatusCode status;
	
	private String contentType;

	@ManyToOne
	@JoinColumn(name = "PRODUCT_ID",nullable = true)
	private Product product;
}
