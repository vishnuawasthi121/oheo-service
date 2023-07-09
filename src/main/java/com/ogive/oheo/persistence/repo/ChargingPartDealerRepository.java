package com.ogive.oheo.persistence.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.ogive.oheo.persistence.entities.ChargingPartDealer;

public interface ChargingPartDealerRepository
		extends CrudRepository<ChargingPartDealer, Long>, JpaSpecificationExecutor<ChargingPartDealer> {

	List<Object[]> dropDown();
}
