package com.ogive.oheo.persistence.repo;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.ogive.oheo.persistence.entities.LeaseDetail;

public interface LeaseDetailRepository
		extends CrudRepository<LeaseDetail, Long>, JpaSpecificationExecutor<LeaseDetail> {

}
