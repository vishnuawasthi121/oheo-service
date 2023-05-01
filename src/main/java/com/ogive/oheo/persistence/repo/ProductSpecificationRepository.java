package com.ogive.oheo.persistence.repo;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.ogive.oheo.persistence.entities.ProductSpecification;

public interface ProductSpecificationRepository extends CrudRepository<ProductSpecification, Long>, JpaSpecificationExecutor<ProductSpecification> {

}
