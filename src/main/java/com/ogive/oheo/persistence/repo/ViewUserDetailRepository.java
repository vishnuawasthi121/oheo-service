package com.ogive.oheo.persistence.repo;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.ogive.oheo.persistence.entities.ViewUserDetails;

public interface ViewUserDetailRepository
		extends CrudRepository<ViewUserDetails, Long>, JpaSpecificationExecutor<ViewUserDetails> {

	ViewUserDetails findByEmail(String email);
}
