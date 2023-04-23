package com.ogive.oheo.persistence.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.ogive.oheo.persistence.entities.PurchaseType;
import com.ogive.oheo.persistence.entities.VehicleDetail;

public interface PurchaseTypeRepository
		extends CrudRepository<PurchaseType, Long>, JpaSpecificationExecutor<PurchaseType> {

	List<Object[]> dropDown();

}
