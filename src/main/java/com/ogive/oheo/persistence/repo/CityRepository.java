package com.ogive.oheo.persistence.repo;

import org.springframework.data.repository.CrudRepository;

import com.ogive.oheo.persistence.entities.City;
import com.ogive.oheo.persistence.entities.State;

public interface CityRepository extends CrudRepository<City, Long> {

	City findByStateAndId(State state,Long id);
}
