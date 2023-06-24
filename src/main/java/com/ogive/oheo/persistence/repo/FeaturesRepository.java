package com.ogive.oheo.persistence.repo;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;

import com.ogive.oheo.persistence.entities.Features;

public interface FeaturesRepository extends CrudRepository<Features, Long> {
	
	@Modifying
	void deleteByProductId(long productId);
}
