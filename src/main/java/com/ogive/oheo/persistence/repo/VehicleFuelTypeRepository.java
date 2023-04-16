package com.ogive.oheo.persistence.repo;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.ogive.oheo.persistence.entities.VehicleFuelType;

public interface VehicleFuelTypeRepository extends CrudRepository<VehicleFuelType, Long>, JpaSpecificationExecutor<VehicleFuelType> {

}
