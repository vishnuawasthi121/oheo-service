package com.ogive.oheo.persistence.repo;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.ogive.oheo.persistence.entities.ChargingSlotBookingRequest;

public interface ChargingSlotBookingRequestRepository  extends CrudRepository<ChargingSlotBookingRequest, Long>, JpaSpecificationExecutor<ChargingSlotBookingRequest> {

}
