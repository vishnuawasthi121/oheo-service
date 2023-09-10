package com.ogive.oheo.shoppingcart;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

import com.ogive.oheo.dto.FilterCriteria;
import com.ogive.oheo.persistence.entities.CartItemEntity;
import com.ogive.oheo.persistence.entities.ShoppingCartEntity;

public class ShoppingCartSpecification {

	
public static Specification<CartItemEntity> fetchShoppingCartItemsByCartId(Long shoopingCartId) {
		
		return new Specification<CartItemEntity>() {
			@Override
			public Predicate toPredicate(Root<CartItemEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				
				Join<CartItemEntity, ShoppingCartEntity> shoppingCart = root.join("shoppingCart");
				return criteriaBuilder.equal(shoppingCart.get("id"), shoopingCartId);
			}
		};
	}
}
