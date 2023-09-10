package com.ogive.oheo.persistence.repo;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.ogive.oheo.persistence.entities.ShoppingCartEntity;

public interface ShoppingCartEntityRepository extends CrudRepository<ShoppingCartEntity, Long>, JpaSpecificationExecutor<ShoppingCartEntity> {

}
