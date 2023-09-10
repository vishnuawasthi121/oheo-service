package com.ogive.oheo.shoppingcart;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {

	List<CartItem> cartItems = new ArrayList<CartItem>();

	private BigDecimal totalCartValue;

	private void addToCart(CartItem cartItem) {
		cartItems.add(cartItem);
	}

	private void removeFromCart(CartItem cartItem) {
		cartItems.remove(cartItem);
	}

}
