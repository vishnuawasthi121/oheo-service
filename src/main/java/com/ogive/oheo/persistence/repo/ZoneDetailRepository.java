package com.ogive.oheo.persistence.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.ogive.oheo.persistence.entities.Country;
import com.ogive.oheo.persistence.entities.ZoneDetail;

public interface ZoneDetailRepository extends CrudRepository<ZoneDetail, Long>, JpaSpecificationExecutor<ZoneDetail> {

	ZoneDetail findZoneDetailByIdAndCountry(Long id, Country country);

	List<ZoneDetail> findByCountry(Country country);

	void deleteByCountryAndId(Country country, Long id);
}
