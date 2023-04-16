package com.ogive.oheo.persistence.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ogive.oheo.persistence.entities.City;
import com.ogive.oheo.persistence.entities.State;

public interface CityRepository extends PagingAndSortingRepository<City, Long> {

	City findByStateAndId(State state, Long id);

	Page<City> findByNameContaining(String name, Pageable pageable);
}
