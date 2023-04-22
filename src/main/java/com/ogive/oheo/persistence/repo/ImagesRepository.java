package com.ogive.oheo.persistence.repo;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.ogive.oheo.persistence.entities.Images;

public interface ImagesRepository extends CrudRepository<Images, Long> , JpaSpecificationExecutor<Images> {

}