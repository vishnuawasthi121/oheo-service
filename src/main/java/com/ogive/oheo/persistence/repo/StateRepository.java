package com.ogive.oheo.persistence.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.ogive.oheo.persistence.entities.Country;
import com.ogive.oheo.persistence.entities.State;

public interface StateRepository extends CrudRepository<State, Long> {

	State findByCountryAndId(Country country,Long id);
	
	List<State> findByCountry(Country country);
	
	void deleteByCountryAndId(Country country, Long id);
	
	
}
