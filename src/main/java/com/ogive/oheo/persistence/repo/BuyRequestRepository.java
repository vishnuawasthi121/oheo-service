package com.ogive.oheo.persistence.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;

import com.ogive.oheo.persistence.entities.BuyRequest;

public interface BuyRequestRepository extends CrudRepository<BuyRequest, Long>, JpaSpecificationExecutor<BuyRequest>{
	List<BuyRequest> fetchByRequestByUserId(Long userId);
	
	@Modifying
	void deleteByProductId(long productId);
	
}
