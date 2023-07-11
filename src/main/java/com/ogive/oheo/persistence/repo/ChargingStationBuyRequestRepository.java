package com.ogive.oheo.persistence.repo;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.ogive.oheo.persistence.entities.ChargingStationBuyRequest;

public interface ChargingStationBuyRequestRepository extends CrudRepository<ChargingStationBuyRequest, Long>, JpaSpecificationExecutor<ChargingStationBuyRequest>{

}
