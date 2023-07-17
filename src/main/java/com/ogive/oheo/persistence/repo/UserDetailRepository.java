package com.ogive.oheo.persistence.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;

import com.ogive.oheo.constants.RoleTypes;
import com.ogive.oheo.persistence.entities.UserDetail;

public interface UserDetailRepository extends CrudRepository<UserDetail, Long>, JpaSpecificationExecutor<UserDetail> {

	UserDetail findByEmail(String email);

	List<UserDetail> findAllSubUsersByRootId(Long rootId);

	UserDetail findByEmailAndPassword(String username, String password);

	List<Object[]> dropDown(RoleTypes roleType);
	List<Object[]> dropDownAllUser();
	
	@Modifying
	void upadateVehicleType(Long toVehicleTypeId,Long fromVehicleTypeId);
}
