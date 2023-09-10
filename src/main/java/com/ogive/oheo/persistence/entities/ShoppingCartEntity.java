package com.ogive.oheo.persistence.entities;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

@Table(name = "SHOPPING_CART")
@Entity
public class ShoppingCartEntity {

	@Id
	private Long id;

	@Column
	private String customerName;

	@OneToMany(mappedBy = "shoppingCart")
	private Set<CartItemEntity> cartItems;

}
