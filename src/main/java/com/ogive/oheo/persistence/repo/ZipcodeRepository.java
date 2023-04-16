package com.ogive.oheo.persistence.repo;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.ogive.oheo.persistence.entities.Zipcode;

public interface ZipcodeRepository extends CrudRepository<Zipcode, Long>, JpaSpecificationExecutor<Zipcode> {

	Zipcode findByCode(Long code);

//	List<Zipcode> findByCode(Long code);
}
