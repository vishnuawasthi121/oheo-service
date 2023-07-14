package com.ogive.oheo.persistence.repo;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.ogive.oheo.persistence.entities.ViewLiveProduct;

public interface ViewLiveProductRepository extends CrudRepository<ViewLiveProduct, Long>, JpaSpecificationExecutor<ViewLiveProduct> {

}
