package com.ogive.oheo.persistence.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.ogive.oheo.persistence.entities.Product;

public interface ProductRepository  extends CrudRepository<Product, Long>, JpaSpecificationExecutor<Product>{

	List<Object[]>	dropDownLive(String isLive);
}
