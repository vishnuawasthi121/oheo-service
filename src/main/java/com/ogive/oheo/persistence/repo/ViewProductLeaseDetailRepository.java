package com.ogive.oheo.persistence.repo;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.ogive.oheo.persistence.entities.ViewProductLeaseDetail;

public interface ViewProductLeaseDetailRepository  extends CrudRepository<ViewProductLeaseDetail, Long>, JpaSpecificationExecutor<ViewProductLeaseDetail> {

	
}
