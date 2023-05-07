package com.ogive.oheo.persistence.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.ogive.oheo.persistence.entities.TermAndConditions;

public interface TermAndConditionsRepository extends CrudRepository<TermAndConditions, Long>, JpaSpecificationExecutor<TermAndConditions> {

	Optional<TermAndConditions> findFirstByOrderByCreatedDateDesc();
	
}
