package com.ogive.oheo.shoppingcart;

import java.math.BigDecimal;
import java.util.Objects;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.ogive.oheo.constants.ProductType;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CartItem {

	@NotEmpty
	private String productName;

	@NotNull
	private Long productId;

	private ProductType productType;

	@NotNull
	private BigDecimal price;

	private String descriptionUrl;
	
	
	

	@Override
	public int hashCode() {
		return Objects.hash(productId, productName, productType);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CartItem other = (CartItem) obj;
		return Objects.equals(productId, other.productId) && Objects.equals(productName, other.productName)
				&& Objects.equals(productType, other.productType);
	}

	public CartItem() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CartItem(@NotEmpty String productName, @NotNull Long productId, ProductType productType,
			@NotNull BigDecimal price, String descriptionUrl) {
		super();
		this.productName = productName;
		this.productId = productId;
		this.productType = productType;
		this.price = price;
		this.descriptionUrl = descriptionUrl;
	}
	
	

	/*
	 * public static void main(String args[]) { BigDecimal price = new
	 * BigDecimal("1050.50"); System.out.println("price   : " + price); }
	 */

}
