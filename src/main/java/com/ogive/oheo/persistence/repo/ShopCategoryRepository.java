package com.ogive.oheo.persistence.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.ogive.oheo.persistence.entities.ShopCategory;

public interface ShopCategoryRepository  extends CrudRepository<ShopCategory, Long>, JpaSpecificationExecutor<ShopCategory>{

	// To generate drop down in website - Shop -> Sub menu
	List<Object[]> dropDownAvailableShopSections(List<Object> excludedNames);
}
