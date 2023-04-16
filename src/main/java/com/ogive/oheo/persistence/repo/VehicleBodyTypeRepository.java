package com.ogive.oheo.persistence.repo;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.ogive.oheo.persistence.entities.VehicleBodyType;

public interface VehicleBodyTypeRepository extends CrudRepository<VehicleBodyType, Long> , JpaSpecificationExecutor<VehicleBodyType>  {

	
}
