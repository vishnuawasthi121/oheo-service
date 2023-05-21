package com.ogive.oheo.persistence.repo;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.ogive.oheo.persistence.entities.ChargingProduct;

public interface ChargingProductRepository  extends CrudRepository<ChargingProduct, Long>, JpaSpecificationExecutor<ChargingProduct>{

	ChargingProduct findProductByUserIdAndId(Long userId,Long id);
}
