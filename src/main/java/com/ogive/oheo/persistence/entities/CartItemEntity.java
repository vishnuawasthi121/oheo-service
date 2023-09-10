package com.ogive.oheo.persistence.entities;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.ogive.oheo.constants.ProductType;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

@Table(name = "CART_ITEM")
@SequenceGenerator(allocationSize = 1, initialValue = 100, name = "SEQ_CART_ITEM", sequenceName = "SEQ_CART_ITEM")
@Entity
public class CartItemEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CART_ITEM")
	private Long id;
	
	private String productName;

	private Long productId;

	@Enumerated(EnumType.STRING)
	private ProductType productType;

	private BigDecimal price;

	@PrePersist
	@PreUpdate
	public void pricePrecisionConvertion() {
		// convert your bigdecimal scale to 2 here
		this.price.setScale(2, RoundingMode.HALF_UP);
	}

	@ManyToOne
	@JoinColumn(name = "CART_ID", nullable = false)
	private ShoppingCartEntity shoppingCart;
	
	
	

	public CartItemEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CartItemEntity(String productName, Long productId, ProductType productType, BigDecimal price) {
		super();
		this.productName = productName;
		this.productId = productId;
		this.productType = productType;
		this.price = price;
	}
	
	
}
