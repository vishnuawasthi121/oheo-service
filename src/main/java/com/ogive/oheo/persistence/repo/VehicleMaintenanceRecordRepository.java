package com.ogive.oheo.persistence.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.ogive.oheo.persistence.entities.VehicleMaintenanceRecord;

public interface VehicleMaintenanceRecordRepository
		extends CrudRepository<VehicleMaintenanceRecord, Long>, JpaSpecificationExecutor<VehicleMaintenanceRecord> {

	List<VehicleMaintenanceRecord> findByEmail(String email);

}
