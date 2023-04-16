package com.ogive.oheo.persistence.repo;

import org.springframework.data.repository.CrudRepository;

import com.ogive.oheo.persistence.entities.Address;

//Address
public interface AddressRepository extends CrudRepository<Address, Long>  {

}
