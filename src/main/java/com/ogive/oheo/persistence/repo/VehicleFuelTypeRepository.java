package com.ogive.oheo.persistence.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.ogive.oheo.persistence.entities.VehicleFuelType;

public interface VehicleFuelTypeRepository extends CrudRepository<VehicleFuelType, Long>, JpaSpecificationExecutor<VehicleFuelType> {

	List<Object[]> dropDown();

}
