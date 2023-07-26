package com.ogive.oheo.persistence.entities;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.ogive.oheo.constants.StatusCode;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

@NamedQuery(name="ShopCategory.dropDownAvailableShopSections", query="SELECT id,name from ShopCategory where STATUS  ='ACTIVE' AND name NOT IN (:excludedNames)")
@Table(name = "SHOP_CATEGORY")
@SequenceGenerator(allocationSize = 1, initialValue = 100, name = "SEQ_SHOP_CATEGORY", sequenceName = "SEQ_SHOP_CATEGORY")
@Entity
public class ShopCategory {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SHOP_CATEGORY")
	private Long id;

	private String name;

	@Enumerated(EnumType.STRING)
	private StatusCode status;

	private String description;

}
