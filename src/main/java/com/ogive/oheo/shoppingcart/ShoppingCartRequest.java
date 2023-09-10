package com.ogive.oheo.shoppingcart;

import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ShoppingCartRequest {

	@NotNull
	private Long id;

	@NotEmpty
	private String customerName;

	@NotNull
	@Valid
	private Set<CartItem> cartItems  = new HashSet<>();;

}
