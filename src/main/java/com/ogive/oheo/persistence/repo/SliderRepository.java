package com.ogive.oheo.persistence.repo;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;

import com.ogive.oheo.persistence.entities.Slider;

public interface SliderRepository extends CrudRepository<Slider, Long> {

	@Modifying
	void deleteByProductId(Long productId);
}
