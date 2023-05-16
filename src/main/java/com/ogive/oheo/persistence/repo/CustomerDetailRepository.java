package com.ogive.oheo.persistence.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.ogive.oheo.persistence.entities.CustomerDetail;

public interface CustomerDetailRepository
		extends CrudRepository<CustomerDetail, Long>, JpaSpecificationExecutor<CustomerDetail> {

	CustomerDetail findByEmailIgnoreCaseIn(List<String> email);
}
