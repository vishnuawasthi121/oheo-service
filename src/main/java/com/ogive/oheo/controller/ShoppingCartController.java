package com.ogive.oheo.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ogive.oheo.persistence.entities.CartItemEntity;
import com.ogive.oheo.persistence.entities.ShoppingCartEntity;
import com.ogive.oheo.persistence.repo.CartItemEntityRepository;
import com.ogive.oheo.persistence.repo.ShoppingCartEntityRepository;
import com.ogive.oheo.shoppingcart.ShoppingCartRequest;
import com.ogive.oheo.shoppingcart.ShoppingCartSpecification;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "ShoppingCart")
@RestController
public class ShoppingCartController {
	private static final Logger LOG = LoggerFactory.getLogger(ShoppingCartController.class);

	@Autowired
	private ShoppingCartEntityRepository shoppingCartEntityRepository;

	@Autowired
	private CartItemEntityRepository itemEntityRepository;

	@ApiOperation(value = "Add Item to shopping cart", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(path = "/shopping-cart", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> addItemsToCart(@RequestBody @Valid ShoppingCartRequest cart) {
		LOG.info("addItemsToCart request received@@");
		Optional<ShoppingCartEntity> cartData = shoppingCartEntityRepository.findById(cart.getId());
		ShoppingCartEntity shoppingCartEntity = cartData.isPresent() ? cartData.get() : new ShoppingCartEntity();

		if (!cartData.isPresent()) {
			shoppingCartEntity = shoppingCartEntityRepository.save(shoppingCartEntity);
		}
		List<CartItemEntity> totalItemsInCart = cart.getCartItems().stream().map(request -> {
			// String productName, Long productId, ProductType productType, BigDecimal price
			CartItemEntity cartItemEntity = new CartItemEntity(request.getProductName(), request.getProductId(),
					request.getProductType(), request.getPrice());
			cartItemEntity.setShoppingCart(shoppingCartEntity);
			return cartItemEntity;
		}).collect(Collectors.toList());

		List<CartItemEntity> allCartItems = itemEntityRepository.findAll(ShoppingCartSpecification.fetchShoppingCartItemsByCartId(shoppingCartEntity.getId()));

		return new ResponseEntity<Object>(entity.getId(), HttpStatus.OK);
	}
}
