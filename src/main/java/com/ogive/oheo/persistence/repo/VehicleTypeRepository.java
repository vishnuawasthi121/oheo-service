package com.ogive.oheo.persistence.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.ogive.oheo.persistence.entities.VehicleType;

public interface VehicleTypeRepository extends CrudRepository<VehicleType, Long> , JpaSpecificationExecutor<VehicleType>  {

	List<Object[]> dropDown();

}
