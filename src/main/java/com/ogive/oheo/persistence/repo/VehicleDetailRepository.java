package com.ogive.oheo.persistence.repo;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;

import com.ogive.oheo.persistence.entities.VehicleDetail;

public interface VehicleDetailRepository
		extends CrudRepository<VehicleDetail, Long>, JpaSpecificationExecutor<VehicleDetail> {

	@Modifying
	void upadateVehicleType(Long toVehicleTypeId,Long fromVehicleTypeId);
}
