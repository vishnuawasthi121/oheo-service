package com.ogive.oheo.persistence.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.ogive.oheo.persistence.entities.GroupDetail;

public interface GroupDetailRepository extends CrudRepository<GroupDetail, Long>{

	List<Object[]> dropDown();

}
