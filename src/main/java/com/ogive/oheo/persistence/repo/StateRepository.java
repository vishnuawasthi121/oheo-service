package com.ogive.oheo.persistence.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ogive.oheo.persistence.entities.Country;
import com.ogive.oheo.persistence.entities.State;

public interface StateRepository extends PagingAndSortingRepository<State, Long> {

	State findByCountryAndId(Country country,Long id);
	
	List<State> findByCountry(Country country);
	
	void deleteByCountryAndId(Country country, Long id);
	
	Page<State> findByStateNameContaining(String stateName, Pageable pageable);
	
	
}
