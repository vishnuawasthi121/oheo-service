package com.ogive.oheo.persistence.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.ogive.oheo.persistence.entities.UserRole;

public interface UserRoleRepository extends CrudRepository<UserRole, Long>, JpaSpecificationExecutor<UserRole> {
	
	List<Object[]> dropDown();
}
