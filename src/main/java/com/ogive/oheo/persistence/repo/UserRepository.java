package com.ogive.oheo.persistence.repo;

import org.springframework.data.repository.CrudRepository;

import com.ogive.oheo.persistence.entities.UserInfo;

public interface UserRepository extends CrudRepository<UserInfo, Long> {

}
