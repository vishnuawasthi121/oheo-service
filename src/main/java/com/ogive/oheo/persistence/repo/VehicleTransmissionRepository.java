package com.ogive.oheo.persistence.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.ogive.oheo.persistence.entities.VehicleTransmission;

public interface VehicleTransmissionRepository extends CrudRepository<VehicleTransmission, Long> , JpaSpecificationExecutor<VehicleTransmission> {

	List<Object[]> dropDown();

}
