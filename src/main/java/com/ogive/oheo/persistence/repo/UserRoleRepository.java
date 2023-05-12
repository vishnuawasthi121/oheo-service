package com.ogive.oheo.persistence.repo;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.ogive.oheo.persistence.entities.UserRole;

public interface UserRoleRepository extends CrudRepository<UserRole, Long>, JpaSpecificationExecutor<UserRole> {

}
