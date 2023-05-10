package com.ogive.oheo.persistence.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.ogive.oheo.persistence.entities.VehicleModel;

public interface VehicleModelRepository extends CrudRepository<VehicleModel, Long>, JpaSpecificationExecutor<VehicleModel> {

	List<Object[]> dropDown();
	
	List<Object[]> dropDownByCompanyId(Long companyId);

}
