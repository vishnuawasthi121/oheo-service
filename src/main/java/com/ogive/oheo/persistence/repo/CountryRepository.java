package com.ogive.oheo.persistence.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.ogive.oheo.persistence.entities.Country;

public interface CountryRepository extends CrudRepository<Country, Long> {

	Country findByCountryCode(String countryCode);

	List<Object[]> dropDown();
}
